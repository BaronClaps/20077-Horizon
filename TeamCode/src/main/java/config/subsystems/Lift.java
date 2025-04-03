package config.subsystems;

import static config.core.RobotConstants.liftAfterHighChamber;
import static config.core.RobotConstants.liftToHighBucket;
import static config.core.RobotConstants.liftToHighChamber;
import static config.core.RobotConstants.liftToPark;
import static config.core.RobotConstants.liftToZero;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import config.core.hardware.CachedMotor;

/** @author Baron Henderson
 * @version 2.0 | 1/4/25
 */

@Config
public class Lift {

    private Telemetry telemetry;
    public CachedMotor rightLift, leftLift;
    public int pos;
    public PIDController pid;
    public int pidLevel = 0;
    public static int target;
    public static double p = 0.01, i = 0, d = 0.00000000000005, f = 0.05;


    public Lift(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        rightLift = new CachedMotor(hardwareMap.get(DcMotor.class, "rightLift"));
        leftLift = new CachedMotor(hardwareMap.get(DcMotor.class, "leftLift"));

        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setDirection(DcMotor.Direction.REVERSE);
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pid = new PIDController(p, i, d);
    }

    public void update() {
        if(pidLevel == 1) {
            pid.setPID(p, i, d);

            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            double pid_output = pid.calculate(getPos(), target);
            double power = pid_output + f;

            if (getPos() < 50 && target < 50) {
                rightLift.setPower(0);
                leftLift.setPower(0);
            } else {
                rightLift.setPower(power);
                leftLift.setPower(power);
            }
        } else if (pidLevel == 2){
            target = getPos();
            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            target = getPos();
            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightLift.setPower(0);
            leftLift.setPower(0);
        }
    }

    public void manual(double left, double right) {
        if(Math.abs(left) > 0.05 || Math.abs(right) > 0.05) {
            pidLevel = 2;
            leftLift.setPower(right - left);
            rightLift.setPower(right - left);
        }
    }

    public void setTarget(int b) {
        pidLevel = 1;
        target = b;
    }

    public int getPos() {
        pos = leftLift.getPosition();
        return leftLift.getPosition();
    }

    public void init() {
        pid.setPID(p,i,d);
    }

    public void start() {
        target = 0;
    }

    public void toZero() {
        setTarget(liftToZero);
    }

    public void toHighBucket() {
        setTarget(liftToHighBucket);
    }

    public void toChamber() {
        setTarget(liftToHighChamber);
    }

    public void toChamberScore() {
        setTarget(liftAfterHighChamber);
    }

    public void toPark() {
        setTarget(liftToPark);
    }

    public boolean roughlyAtTarget() {
        return Math.abs(getPos() - target) < 25;
    }

    public void pidOn() {
        pidLevel = 1;
    }

    public void pidOff() {
        pidLevel = 2;
    }

    public void telemetry() {
        telemetry.addData("Lift Pos: ", getPos());
        telemetry.addData("Lift Target: ", target);
    }

    public void periodic() {
        update();
        telemetry();
    }
}
