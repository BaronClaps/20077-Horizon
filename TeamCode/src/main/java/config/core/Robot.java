package config.core;

import static config.core.Opmode.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

import config.pedro.constants.FConstants;
import config.pedro.constants.LConstants;
import config.subsystems.Extend;
import config.subsystems.Intake;
import config.subsystems.Lift;
import config.subsystems.Outtake;

public class Robot {
    private HardwareMap h;
    private Telemetry t;
    private Gamepad g1a, g2a, g1, g2, p1, p2;
    private Alliance a;
    private Follower f;
    private Extend e;
    private Intake i;
    private Lift l;
    private Outtake o;
    private Opmode op = TELEOP;
    public static Pose autoEndPose = new Pose(0,0,0);
    public double speed = 0.75;
    public Timer tTimer, sTimer, spec0Timer, spec180Timer, c0Timer, c180Timer;
    public int flip = 1, tState = -1, sState = -1, spec0State = -1, spec180State = -1, c0State = -1, c180State = -1;

    public Robot(HardwareMap h, Telemetry t, Gamepad g1a, Gamepad g2a, Alliance a, Pose startPose) {
        this.op = TELEOP;
        this.h = h;
        this.t = t;
        this.g1a = g1a;
        this.g2a = g2a;
        this.a = a;

        Constants.setConstants(FConstants.class, LConstants.class);

        f = new Follower(this.h);
        f.setStartingPose(startPose);

        e = new Extend(this.h,this.t);
        l = new Lift(this.h,this.t);
        i = new Intake(this.h,this.t);
        o = new Outtake(this.h,this.t);

        this.g1 = new Gamepad();
        this.g2 = new Gamepad();
        this.p1 = new Gamepad();
        this.p2 = new Gamepad();

        tTimer = new Timer();
        sTimer = new Timer();
        spec0Timer = new Timer();
        spec180Timer = new Timer();
        c0Timer = new Timer();
        c180Timer = new Timer();

        List<LynxModule> allHubs = h.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    public Robot(HardwareMap h, Telemetry t, Alliance a, Pose startPose) {
        this.op = AUTONOMOUS;
        this.h = h;
        this.t = t;
        this.a = a;

        Constants.setConstants(FConstants.class, LConstants.class);

        f = new Follower(this.h);
        f.setStartingPose(startPose);

        e = new Extend(this.h,this.t);
        l = new Lift(this.h,this.t);
        i = new Intake(this.h,this.t);
        o = new Outtake(this.h,this.t);

        tTimer = new Timer();
        sTimer = new Timer();
        spec0Timer = new Timer();
        spec180Timer = new Timer();
        c0Timer = new Timer();
        c180Timer = new Timer();

        List<LynxModule> allHubs = h.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    public void aPeriodic() {
        chamber180();
        specimen180();
        chamber0();
        specimen0();
        submersible();
        transfer();

        t.addData("path", f.getCurrentPath());
        f.telemetryDebug(t);

        e.periodic();
        l.periodic();
        i.periodic();
        o.periodic();
        f.update();
        t.update();
    }

    public void tPeriodic() {
        updateControls();
        submersible();
        transfer();

        e.periodic();
        l.periodic();
        i.periodic();
        o.periodic();
        f.update();
        t.update();
    }

    public void tStart() {
        o.start();
        f.startTeleopDrive();
    }

    public void stop() {
        autoEndPose = f.getPose();
    }

    public void updateControls() {
        p1.copy(g1);
        p2.copy(g2);
        g1.copy(g1a);
        g2.copy(g2a);

        if (g1.right_bumper)
            speed = 1;
        else if (g1.left_bumper)
            speed = 0.25;
        else
            speed = 0.75;

        l.manual(g2.left_trigger, g2.right_trigger);

        if (g1.x) {
            flip = -1;
        }

        if (g1.b) {
            flip = 1;
        }

        if (g1.right_trigger > 0.1)
            e.toFull();

        if (g1.left_trigger > 0.1)
            e.toZero();

        if (g2.a && !p2.a)
            o.switchGrabState();

        if (g2.y && !p2.y) {
            o.transfer();
            i.hover();
        }

        if (g2.x && !p2.x) {
            o.score();
            i.hover();
        }

        if (g2.dpad_left && !p2.dpad_left) {
            o.specimenGrab180();
            i.specimen();
        }

        if (g2.dpad_right && !p2.dpad_right) {
            o.specimenScore180();
            i.specimen();
        }

        if (g2.b && !p2.b)
           startTransfer();

        if (g2.dpad_up && !p2.dpad_up)
            i.switchGrabState();

        if (!g2.dpad_down && p2.dpad_down)
            startSubmersible();


        if (g2.dpad_down && !p2.dpad_down) {
            i.cloud();
            i.open();
            o.transfer();
        }

        if (g2.left_bumper && !p2.left_bumper)
            i.rotateCycleLeft();

        if (g2.right_bumper && !p2.right_bumper)
            i.rotateCycleRight();

        if (g2.left_stick_button) {
            o.hang();
            i.transfer();
            e.toZero();
        }

        if (g2.right_stick_button)
            i.transfer();
        
        f.setTeleOpMovementVectors(flip * -g1.left_stick_y * speed, flip * -g1.left_stick_x * speed, -g1.right_stick_x * speed * 0.5, true);
    }

    public HardwareMap getH() {
        return h;
    }

    public Telemetry getT() {
        return t;
    }


    public Gamepad getG1() {
        return g1;
    }

    public Gamepad getG2() {
        return g2;
    }

    public Gamepad getP1() {
        return p1;
    }

    public Gamepad getP2() {
        return p2;
    }

    public Alliance getA() {
        return a;
    }

    public void setA(Alliance a) {
        this.a = a;
    }

    public Follower getF() {
        return f;
    }

    public Extend getE() {
        return e;
    }

    public Intake getI() {
        return i;
    }

    public void setI(Intake i) {
        this.i = i;
    }

    public Lift getL() {
        return l;
    }

    public Outtake getO() {
        return o;
    }

    public void slowDrive() {
        speed = 0.25;
    }

    public void normalDrive() {
        speed = 0.75;
    }

    public void fastDrive() {
        speed = 0.25;
    }

    public void flip() {
        flip = -1;
    }

    public void unflip() {
        flip = 1;
    }


    private void transfer() {
        t.addData("Transfer State", tState);

        switch (tState) {
            case 1:
                i.close();
                //     transferSampleDetected = (intake.getColor() == IntakeColor.BLUE || intake.getColor() == IntakeColor.RED || intake.getColor() == IntakeColor.YELLOW);
                o.transfer();
                i.transfer();
                setTransferState(2);
                break;
            case 2:
                if (tTimer.getElapsedTimeSeconds() > 0.1) {
                    o.setRotateState(Outtake.RotateState.TRANSFER);
                    e.toZero();
                    setTransferState(3);
                }
                break;
            case 3:
                if (tTimer.getElapsedTimeSeconds() > 0.15) {
                    o.transfer();
                    setTransferState(4);
                }
                break;
            case 4:
                if (tTimer.getElapsedTimeSeconds() > 0) {
                    o.close();
                    setTransferState(5);
                }
                break;
            case 5:
                if (tTimer.getElapsedTimeSeconds() > 0.5) {
                    o.score();
                    setTransferState(6);
                }
                break;
            case 6:
                if (tTimer.getElapsedTimeSeconds() > 0) {
                    i.open();
                    setTransferState(7);
                }
                break;
            case 7:
                if (tTimer.getElapsedTimeSeconds() > 0.25) {
                    i.hover();
                    setTransferState(-1);
                }
                break;
        }
    }

    public void setTransferState(int x) {
        tState = x;
        tTimer.resetTimer();
    }

    public void startTransfer() {
        setTransferState(1);
    }

    private void submersible() {
        t.addData("Submersible State", sState);

        switch (sState) {
            case 0:
                i.ground();
                i.open();
                o.transfer();
                setSubmersibleState(1);
                break;
            case 1:
                if(sTimer.getElapsedTimeSeconds() > 0.1) {
                    i.close();
                    setSubmersibleState(2);
                }
                break;
            case 2:
                if (sTimer.getElapsedTimeSeconds() > 0.15) {
                    i.hover();
                    setSubmersibleState(-1);
                }
                break;
        }
    }

    public void setSubmersibleState(int x) {
        sState = x;
        sTimer.resetTimer();
    }

    public void startSubmersible() {
        setSubmersibleState(0);
    }

    public void chamber180() {
        t.addData("Chamber 180 State", c180State);

        switch (c180State) {
            case 1:
                o.specimenScore180();
                e.toZero();
                setChamber180State(2);
                break;
            case 2:
                if (!f.isBusy()) {
                    o.open();
                    setChamber180State(3);
                }
                break;
            case 3:
                if(c180Timer.getElapsedTimeSeconds() > 0.25) {
                    o.hang();
                    o.open();
                    setChamber180State(-1);
                }
                break;
        }
    }

    public void setChamber180State(int x) {
        c180State = x;
        c180Timer.resetTimer();
    }

    public void startChamber180() {
        setChamber180State(1);
    }

    public void specimen180() {
        t.addData("Specimen 180 State", spec180State);
        if (spec180State == 1) {
            o.specimenGrab180();
            e.toZero();
            setSpecimen180State(-1);
        }
    }

    public void setSpecimen180State(int x) {
        spec180State = x;
        spec180Timer.resetTimer();
    }

    public void startSpecimen180() {
        setSpecimen180State(1);
    }

    public void chamber0() {
        t.addData("Chamber 0 State", c0State);

        switch (c0State) {
            case 1:
                l.toChamberScore();
                setChamber0State(2);
                break;
            case 2:
                if (l.roughlyAtTarget()) {
                    o.open();
                    setChamber0State(3);
                }
                break;
            case 3:
                if(c0Timer.getElapsedTimeSeconds() > 0.25) {
                    l.toZero();
                    setChamber0State(-1);
                }
                break;
        }
    }

    public void setChamber0State(int x) {
        c180State = x;
        c180Timer.resetTimer();
    }

    public void startChamber0() {
        setChamber0State(1);
    }

    public void specimen0() {
        t.addData("Specimen 0 State", spec0State);

        switch (spec0State) {
            case 1:
                o.specimenGrab0();
                e.toZero();
                setSpecimen0State(2);
                break;
            case 2:
                if (!f.isBusy()) {
                    o.close();
                    setSpecimen0State(3);
                }
                break;
            case 3:
                if(spec0Timer.getElapsedTimeSeconds() > 0.25) {
                    l.toChamber();
                    o.specimenScore0();
                    setSpecimen0State(-1);
                }
                break;
        }
    }

    public void setSpecimen0State(int x) {
        spec0State = x;
        spec0Timer.resetTimer();
    }

    public void startSpecimen0() {
        setSpecimen0State(1);
    }
}
