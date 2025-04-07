package opmode.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.NewFollower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import config.core.paths.FourSamp;
import config.core.paths.SevenSpec;
import config.core.paths.SixSpecPush;
import config.pedro.constants.FConstants;
import config.pedro.constants.LConstants;
import config.subsystems.Extend;
import config.subsystems.Intake;
import config.subsystems.Outtake;


@TeleOp(name = "Drive Test", group = "Tests")
public class DriveTest extends OpMode {
    NewFollower f;
    Intake i;
    Extend e;
    Outtake o;

    @Override
    public void init() {
        f = new NewFollower(hardwareMap, FConstants.class, LConstants.class);
        f.setStartingPose(SevenSpec.start);

        i = new Intake(hardwareMap, telemetry);
        e = new Extend(hardwareMap, telemetry);
        o = new Outtake(hardwareMap, telemetry);

        e.toFull();
        o.specimenScore0();
        i.hover();
    }

    @Override
    public void loop()
    {
        f.update();
        i.periodic();
        telemetry.addData("Pose", f.getPose());
        telemetry.update();
    }
}
