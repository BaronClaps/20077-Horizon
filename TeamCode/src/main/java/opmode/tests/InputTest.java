package opmode.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import config.core.ManualInput;
import config.core.ManualPose;
import config.core.Robot;
import config.core.paths.SevenSpec;
import config.core.util.Alliance;
import config.pedro.constants.FConstants;
import config.pedro.constants.LConstants;

@TeleOp(name = "Input Test")
public class InputTest extends OpMode {
    private ManualInput manualInput;
    private Robot r;
    private Gamepad g1, p1;

    @Override
    public void init() {
        r = new Robot(hardwareMap, telemetry, g1, gamepad2, Alliance.BLUE, SevenSpec.start);
        manualInput = new ManualInput(telemetry, gamepad1, 1, true);
        g1 = new Gamepad();
        p1 = new Gamepad();
    }

    @Override
    public void loop() {

        p1.copy(g1);
        g1.copy(gamepad1);

        if (g1.left_trigger > 0.1 && p1.left_trigger < 0.1) {
            ManualPose temp = manualInput.getCurrent();
            r.getI().rotateDegrees(temp.getRotation());
            r.getE().toFull();
            Path tempp = new Path(new BezierLine(r.getF().getPose(), temp.getPose()));
            tempp.setConstantHeadingInterpolation(0);
            r.getF().followPath(tempp, true);
        }
        manualInput.update(g1);
        telemetry.addData("Current Rotation", manualInput.getCurrent().getRotation());
        telemetry.addData("Current Pose", manualInput.getCurrent().getPose());
        telemetry.addLine();
        r.aPeriodic();

    }

}
