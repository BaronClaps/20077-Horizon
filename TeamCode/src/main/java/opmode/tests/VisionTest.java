package opmode.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import config.core.ManualInput;
import config.core.paths.SixSpecPush;
import config.pedro.constants.FConstants;
import config.pedro.constants.LConstants;
import config.subsystems.Extend;
import config.subsystems.Intake;
import config.subsystems.Light;
import config.subsystems.Outtake;
import config.vision.limelight.Vision;

@TeleOp(group = "Tests", name = "Vision Test")
public class VisionTest extends OpMode {
    Vision v;
    Follower f;
    Extend e;
    Intake i;
    Light j;
    Outtake o;
    Gamepad g1, p1;
    
    public int sState;
    public Timer sTimer, timer;

    @Override
    public void init() {
        f = new Follower(hardwareMap, FConstants.class, LConstants.class);
        v = new Vision(hardwareMap, telemetry, new int[]{1, 2}, f);
        e = new Extend(hardwareMap, telemetry);
        i = new Intake(hardwareMap, telemetry);
        j = new Light(hardwareMap, telemetry);
        o = new Outtake(hardwareMap, telemetry);

        f.setStartingPose(SixSpecPush.score1);

        g1 = new Gamepad();
        p1 = new Gamepad();

        sTimer = new Timer();
        timer = new Timer();
        
        o.score();

        j.max();
    }

    @Override
    public void init_loop() {
//        manualInput.update(gamepad2);
        telemetry.update();
    }

    @Override
    public void loop() {
        p1.copy(g1);
        g1.copy(gamepad1);

        v.find();

        if (g1.y && !p1.y) {
            v.find();
            i.rotateDegrees(v.getAngle());
            e.toFull();
            f.followPath(v.toTarget());
        }

        if (g1.dpad_left) {
            if (timer.getElapsedTimeSeconds() > 0.25) {
                v.find();
                i.rotateDegrees(v.getAngle());
                f.followPath(v.toTarget());
                timer.resetTimer();
            }
        }

        if (g1.dpad_up && !p1.dpad_up)
            j.switchI();

        if (g1.a && !p1.a)
            i.rotateDegrees(v.getAngle());

        if(g1.right_bumper && !p1.right_bumper) {
            f.followPath(v.toTarget());
        }

        if(g1.left_bumper && !p1.left_bumper) {
            e.toFull();
        }

        if (!g1.dpad_down && p1.dpad_down)
            startSubmersible();

        if (g1.dpad_down && !p1.dpad_down) {
            i.cloud();
            i.open();
        }

        f.update();
        submersible();
        telemetry.update();


    }

    private void submersible() {
        telemetry.addData("Submersible State", sState);

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

}
