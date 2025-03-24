package opmode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import config.subsystems.Lift;
import config.subsystems.Outtake;

@Config
@TeleOp(group = "Tests", name = "Lift Test")
public class LiftTest extends OpMode {
    public static int target = 0;

    Lift l;
    Outtake o;
    Gamepad g1, p1;

    @Override
    public void init() {
        l = new Lift(hardwareMap, telemetry);
        o = new Outtake(hardwareMap, telemetry);

        g1 = new Gamepad();
        p1 = new Gamepad();

        o.specimenScore0();
    }

    @Override
    public void loop() {
        p1.copy(g1);
        g1.copy(gamepad1);

        if (g1.a)
            l.setTarget(target);
        else {
            l.manual(gamepad1.left_trigger, gamepad1.right_trigger);
        }

        if (g1.b && !p1.b)
            o.specimenScore0After();

        if (g1.x && !p1.x)
            o.specimenScore0();

        l.periodic();
        telemetry.update();
    }
}
