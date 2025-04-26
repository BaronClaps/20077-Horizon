package opmode;

import static config.core.Robot.autoEndPose;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import config.core.util.Alliance;
import config.core.Robot;

@TeleOp(name = "Solo Drive", group = "...Sigma")
public class SoloDrive extends OpMode {

    Robot r;

    @Override
    public void init() {
        r = new Robot(hardwareMap, telemetry, gamepad1 , gamepad2, Alliance.BLUE, autoEndPose, false);
    }

    @Override
    public void start() {
        r.tStart();
    }

    @Override
    public void loop() {
        r.soloControls();
        r.tPeriodic();
    }
}
