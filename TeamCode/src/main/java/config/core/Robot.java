package config.core;

import static config.core.util.Opmode.*;

import android.sax.StartElementListener;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import config.core.util.Alliance;
import config.core.util.Opmode;
import config.pedro.constants.FConstants;
import config.pedro.constants.LConstants;
import config.subsystems.Extend;
import config.subsystems.Intake;
import config.subsystems.Lift;
import config.subsystems.Light;
import config.subsystems.Outtake;
import config.vision.limelight.Vision;

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
    private Light j;
    private ManualInput m;
    private Vision v;
    private Opmode op = TELEOP;
    public static Pose autoEndPose = new Pose();

    public Pose s = new Pose();
    public double speed = 0.9;
    public Timer tTimer, sTimer, spec0Timer, spec180Timer, c0Timer, aFGTimer, aInitLoopTimer, sTTimer, fSATimer;
    public int flip = 1, tState = -1, sState = -1, spec0State = -1, spec180State = -1, c0State = -1, aFGState = -1, specTransferState = -1, fSAState = -1;
    private boolean aInitLoop, frontScore = false, backScore = true, automationActive = false;

    public Robot(HardwareMap h, Telemetry t, Gamepad g1a, Gamepad g2a, Alliance a, Pose startPose) {
        this.op = TELEOP;
        this.h = h;
        this.t = t;
        this.g1a = g1a;
        this.g2a = g2a;
        this.a = a;

        f = new Follower(this.h, FConstants.class, LConstants.class);
        f.setStartingPose(startPose);

        e = new Extend(this.h,this.t);
        l = new Lift(this.h,this.t);
        i = new Intake(this.h,this.t);
        o = new Outtake(this.h,this.t);
        j = new Light(this.h, this.t);

        this.g1 = new Gamepad();
        this.g2 = new Gamepad();
        this.p1 = new Gamepad();
        this.p2 = new Gamepad();

        tTimer = new Timer();
        sTimer = new Timer();
        spec0Timer = new Timer();
        spec180Timer = new Timer();
        c0Timer = new Timer();
        aFGTimer = new Timer();
        sTTimer = new Timer();
        fSATimer = new Timer();
    }

    public Robot(HardwareMap h, Telemetry t, Alliance a, Pose startPose) {
        this.op = AUTONOMOUS;
        this.h = h;
        this.t = t;
        this.a = a;
        this.s = startPose.copy();

        f = new Follower(this.h, FConstants.class, LConstants.class);
        f.setStartingPose(startPose);
        
        e = new Extend(this.h,this.t);
        l = new Lift(this.h,this.t);
        i = new Intake(this.h,this.t);
        o = new Outtake(this.h,this.t);
        m = new ManualInput(this.t, this.g2, 0, true);
     //   v = new Vision(this.h, this.t, a == Alliance.BLUE ? new int[]{1,2} : new int[]{0,2});

        this.g2 = new Gamepad();
        this.p2 = new Gamepad();

        tTimer = new Timer();
        sTimer = new Timer();
        spec0Timer = new Timer();
        spec180Timer = new Timer();
        c0Timer = new Timer();
        aFGTimer = new Timer();
        aInitLoopTimer = new Timer();
        sTTimer = new Timer();
        fSATimer = new Timer();

        aInitLoopTimer.resetTimer();
        aInitLoop = false;
        o.close();
        t.addData("s", s);
        t.addData("status", "not ready");
    }

    public Robot(HardwareMap h, Telemetry t, Alliance a, Pose startPose, boolean spec, int subSamples) {
        this.op = AUTONOMOUS;
        this.h = h;
        this.t = t;
        this.a = a;
        this.s = startPose.copy();

        f = new Follower(this.h, FConstants.class, LConstants.class);
        f.setStartingPose(startPose);

        e = new Extend(this.h,this.t);
        l = new Lift(this.h,this.t);
        i = new Intake(this.h,this.t);
        o = new Outtake(this.h,this.t);
        m = new ManualInput(this.t, this.g2, subSamples, spec);

        this.g2 = new Gamepad();
        this.p2 = new Gamepad();

        tTimer = new Timer();
        sTimer = new Timer();
        spec0Timer = new Timer();
        spec180Timer = new Timer();
        c0Timer = new Timer();
        aFGTimer = new Timer();
        aInitLoopTimer = new Timer();
        sTTimer = new Timer();
        fSATimer = new Timer();

        aInitLoopTimer.resetTimer();
        aInitLoop = false;
        o.close();
        t.addData("s", s);
        t.addData("status", "not ready");
    }

    public void aPeriodic() {
        t.addData("path", f.getCurrentPath());

        e.periodic();
        l.periodic();
        i.periodic();
        o.periodic();
        f.update();
        t.update();
    }

    public void aInitLoop(Gamepad g2a) {
//        if (!aInitLoop) {
//            if (aInitLoopTimer.getElapsedTimeSeconds() > 2) {
//                o.init();
//                t.addData("status", "ready to verify");
//                aInitLoop = true;
//            }
//        }
//
//        p2.copy(g2);
//        g2.copy(g2a);

        m.update(g2a);
        t.update();
    }

    public void tPeriodic() {
        updateControls();
        submersible();
        transfer();
        specTransfer();
        frontScoreAfter();

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
        o.score();
        o.open();
        i.cloud();
      //  v.off();
        autoEndPose = f.getPose();
    }

    public void updateControls() {
        p1.copy(g1);
        p2.copy(g2);
        g1.copy(g1a);
        g2.copy(g2a);

        if (automationActive) {
            if (g1.back && !p1.back) {
                automationActive = false;
            }
            return;
        }

        if (g1.right_bumper)
            speed = 1;
        else if (g1.left_bumper)
            speed = 0.25;
        else
            speed = 0.9;


        l.manual(g2.left_trigger, g2.right_trigger);



        if (g1.a && !p1.a)
            j.switchI();

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
            startSpecTransfer();
        }

/*
        if (g2.x && !p2.x) {
            o.score();
            i.hover();
        }
*/
 /*       if (g2.dpad_left && !p2.dpad_left) {
            o.startSpecGrab();
            i.specimen();
        }

        if (g2.dpad_right && !p2.dpad_right) {
            o.specimenScore180();
            i.specimen();
        }*/
        
        if (g2.dpad_left && !p2.dpad_left) {
            if (!backScore) {
                o.specimenScore180();
                i.specimen();
                backScore = true;
            } else {
                o.startSpecGrab();
                i.specimen();
                backScore = false;
            }
        }

        if (g2.dpad_right && !p2.dpad_right) {
            if (!frontScore) {
                o.specimenScore0();
                getL().toChamber();
                i.hover();
                frontScore = true;
            } else {
                o.specimenScore0After();
                startFrontScoreAfter();
                frontScore = false;
            }
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
        }

        if (g2.left_bumper && !p2.left_bumper)
            i.rotateCycleLeft();

        if (g2.right_bumper && !p2.right_bumper)
            i.rotateCycleRight();

        if (g2.left_stick_button) {
            o.hang();
            i.specimen();
            e.toZero();
        }

        if (g2.right_stick_button)
            l.toZero();

        if (g2.back) {
            i.drag();
        }
        
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

    public Light getJ() {
        return j;
    }

    public Vision getV() {
        return v;
    }

    public ManualInput getM() {
        return m;
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
                //     transferSampleDetected = (intake.getColor() == IntakeColor.BLUE || intake.getColor() == IntakeColor.RED || intake.getColor() == IntakeColor.YELLOW);
                o.transfer();
                l.toZero();
                i.transfer();
                setTransferState(2);
                break;
            case 2:
                if (tTimer.getElapsedTimeSeconds() > 0.1) {
                    e.toTransfer();
                    setTransferState(3);
                }
                break;
            case 3:
                int temp;

                if (e.getState() == Extend.ExtendState.FULL)
                    temp = 1;
                else
                    temp = 0;

                if (tTimer.getElapsedTimeSeconds() > 0.4 && temp == 0) {
                    o.close();
                    setTransferState(4);
                } else if (tTimer.getElapsedTimeSeconds() > 0.6 && temp == 1) {
                    o.close();
                    setTransferState(4);
                }
                break;
            case 4:
                if (tTimer.getElapsedTimeSeconds() > 0.2) {
                    i.open();
                    setTransferState(5);
                }
                break;
            case 5:
                if (tTimer.getElapsedTimeSeconds() > 0.2) {
                    l.pidOff();
                    o.score();
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
                setSubmersibleState(1);
                break;
            case 1:
                if(sTimer.getElapsedTimeSeconds() > 0.1) {
                    i.close();
                    setSubmersibleState(2);
                }
                break;
            case 2:
                if (sTimer.getElapsedTimeSeconds() > 0.25) {
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

    public void automaticFrontGrab() {
        if (aFGState >= 1) {
            t.addLine("Automatic Front Grab ON");
        }

        switch (aFGState) {
            case 1:
                l.toChamberScore();
                setAutomaticFrontGrabState(2);
                break;
            case 2:
                if (l.roughlyAtTarget()) {
                    o.open();
                    setAutomaticFrontGrabState(-1);
                }
                break;
        }
    }

    public void setAutomaticFrontGrabState(int x) {
        aFGState = x;
        aFGTimer.resetTimer();
    }

    public void startAutomaticFrontGrab() {
        setAutomaticFrontGrabState(1);
    }
    
    public void specTransfer() {
        switch (specTransferState) {
            case 0:
                //     transferSampleDetected = (intake.getColor() == IntakeColor.BLUE || intake.getColor() == IntakeColor.RED || intake.getColor() == IntakeColor.YELLOW);
                getO().transfer();
                getL().toZero();
                getI().hover();
                setSpecTransferState(1);
                break;
            case 1:
                if (getF().getCurrentTValue() >= 0.2) {
                    getI().transfer();
                    setSpecTransferState(2);
                }
                break;
            case 2:
                if (sTTimer.getElapsedTimeSeconds() > 0.1) {
                    getL().pidOff();
                    getE().toTransfer();
                    setSpecTransferState(3);
                }
                break;
            case 3:
                int temp;

                if (getE().getState() == Extend.ExtendState.FULL)
                    temp = 1;
                else
                    temp = 0;

                if (sTTimer.getElapsedTimeSeconds() > 0.4 && temp == 0) {
                    getO().close();
                    setSpecTransferState(4);
                } else if (sTTimer.getElapsedTimeSeconds() > 0.45 && temp == 1) {
                    getO().close();
                    setSpecTransferState(4);
                }
                break;
            case 4:
                if (sTTimer.getElapsedTimeSeconds() > 0.25) {
                    getI().open();
                    setSpecTransferState(5);
                }
                break;
            case 5:
                if (sTTimer.getElapsedTimeSeconds() > 0.2) {
                    getO().specimenGrab0Closed();
                    frontScore = false;
                    setSpecTransferState(-1);
                }
                break;
                
        }
    }
    
    public void setSpecTransferState(int x) {
        specTransferState = x;
        sTTimer.resetTimer();
    }
    
    public void startSpecTransfer() {
        setSpecTransferState(0);
    }

    public void frontScoreAfter() {
        if (fSAState == 1) {
            if(fSATimer.getElapsedTimeSeconds() > 0.35) {
                getO().open();
                getL().toZero();
                setFrontScoreAfterState(-1);
            }
        }
    }

    public void setFrontScoreAfterState(int x) {
        fSAState = x;
        fSATimer.resetTimer();
    }

    public void startFrontScoreAfter() {
        setFrontScoreAfterState(1);
    }


}
