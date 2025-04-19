package config.subsystems;

import static config.core.RobotConstants.*;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/** @author Baron Henderson
 * @version 2.0 | 1/4/25
 */

public class Intake {
    public enum GrabState {
        CLOSED, OPEN
    }

    public enum RotateState {
        TRANSFER, GROUND, HOVER, CLOUD, SPECIMEN, DRAG
    }

    public enum PivotState {
        TRANSFER, GROUND, HOVER, CLOUD, SPECIMEN, DRAG
    }

    public Servo grab, leftRotate, rightRotate, pivot;
    public GrabState grabState;
    public RotateState rotateState;
    public PivotState pivotState;
    private Telemetry telemetry;
    public double rotateDegrees = 0;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        grab = hardwareMap.get(Servo.class, "iG");
        leftRotate = hardwareMap.get(Servo.class, "iLR");
        rightRotate = hardwareMap.get(Servo.class, "iRR");
        pivot = hardwareMap.get(Servo.class, "iP");
        this.telemetry = telemetry;
    }

    public void setRotateState(RotateState state) {
        if (state == RotateState.TRANSFER) {
            leftRotate.setPosition(intakeRotateTransfer);
            rightRotate.setPosition(intakeRotateTransfer);
            this.rotateState = RotateState.TRANSFER;
        } else if (state == RotateState.GROUND) {
            leftRotate.setPosition(intakeRotateGroundVertical + (rotateDegrees * intakeRotatePerDegree));
            rightRotate.setPosition(intakeRotateGroundVertical - (rotateDegrees * intakeRotatePerDegree));
            this.rotateState = RotateState.GROUND;
        } else if (state == RotateState.HOVER) {
            leftRotate.setPosition(intakeRotateHoverVertical + (rotateDegrees * intakeRotatePerDegree));
            rightRotate.setPosition(intakeRotateHoverVertical - (rotateDegrees * intakeRotatePerDegree));
            this.rotateState = RotateState.HOVER;
        } else if (state == RotateState.CLOUD) {
            leftRotate.setPosition(intakeRotateCloudVertical + (rotateDegrees * intakeRotatePerDegree));
            rightRotate.setPosition(intakeRotateCloudVertical - (rotateDegrees * intakeRotatePerDegree));
            this.rotateState = RotateState.CLOUD;
        } else if (state == RotateState.SPECIMEN) {
            leftRotate.setPosition(intakeRotateSpecimen);
            rightRotate.setPosition(intakeRotateSpecimen);
            this.rotateState = RotateState.SPECIMEN;
        } else if (state == RotateState.DRAG) {
            leftRotate.setPosition(intakeRotateDrag);
            rightRotate.setPosition(intakeRotateDrag);
            this.rotateState = RotateState.DRAG;
        }
    }

    public void rotateDegrees(double degrees) {

        degrees = ((degrees % 360) + 360) % 360;
        if (degrees > 180) {
            degrees -= 360;
        }

        if (degrees > 90) {
            degrees = 180 - degrees;
        } else if (degrees < -90) {
            degrees = -180 - degrees;
        }

        this.rotateDegrees = degrees;

        setPivotState(PivotState.CLOUD);
        setRotateState(RotateState.CLOUD);
        setGrabState(GrabState.OPEN);
    }


    public void rotateCycle(boolean b) {
        if (b) {
            if (rotateDegrees < 90)
                rotateDegrees += 45;
        } else {
            if (rotateDegrees > -90)
                rotateDegrees -= 45;
        }

        setPivotState(PivotState.CLOUD);
        setRotateState(RotateState.CLOUD);
        setGrabState(GrabState.OPEN);
    }

    public void setGrabState(GrabState grabState) {
        if (grabState == GrabState.CLOSED) {
            grab.setPosition(intakeGrabClose);
            this.grabState = GrabState.CLOSED;
        } else if (grabState == GrabState.OPEN) {
            grab.setPosition(intakeGrabOpen);
            this.grabState = GrabState.OPEN;
        }
    }

    public void switchGrabState() {
        if (grabState == GrabState.CLOSED) {
            setGrabState(GrabState.OPEN);
        } else if (grabState == GrabState.OPEN) {
            setGrabState(GrabState.CLOSED);
        }
    }

    public void setPivotState(PivotState pivotState) {
        if (pivotState == PivotState.TRANSFER) {
            pivot.setPosition(intakePivotTransfer);
            this.pivotState = PivotState.TRANSFER;
        } else if (pivotState == PivotState.GROUND) {
            pivot.setPosition(intakePivotGround);
            this.pivotState = PivotState.GROUND;
        } else if (pivotState == PivotState.HOVER) {
            pivot.setPosition(intakePivotHover);
            this.pivotState = PivotState.HOVER;
        } else if (pivotState == PivotState.CLOUD) {
            pivot.setPosition(intakePivotCloud);
            this.pivotState = PivotState.CLOUD;
        } else if (pivotState == PivotState.SPECIMEN) {
            pivot.setPosition(intakePivotSpecimen);
            this.pivotState = PivotState.SPECIMEN;
        } else if (pivotState == PivotState.DRAG) {
            pivot.setPosition(intakePivotDrag);
            this.pivotState = PivotState.DRAG;
        }
    }

    public void open() {
        setGrabState(GrabState.OPEN);
    }

    public void close() {
        setGrabState(GrabState.CLOSED);
    }

    public void transfer() {
        rotateDegrees = 0;
        setRotateState(RotateState.TRANSFER);
        setPivotState(PivotState.TRANSFER);
        setGrabState(GrabState.CLOSED);
    }

    public void ground() {
        setGrabState(GrabState.OPEN);
        setRotateState(RotateState.GROUND);
        setPivotState(PivotState.GROUND);
    }

    public void hover() {
        rotateDegrees = 0;
        setPivotState(PivotState.HOVER);
        setRotateState(RotateState.HOVER);
    }

    public void specimen() {
        rotateDegrees = 0;
        setPivotState(PivotState.SPECIMEN);
        setRotateState(RotateState.SPECIMEN);
        setGrabState(GrabState.CLOSED);
    }

    public void cloud() {
        setGrabState(GrabState.OPEN);
        setRotateState(RotateState.CLOUD);
        setPivotState(PivotState.CLOUD);
    }

    public void drag() {
        setGrabState(GrabState.OPEN);
        setRotateState(RotateState.DRAG);
        setPivotState(PivotState.DRAG);
    }

    public void cDrag() {
        setGrabState(GrabState.OPEN);
        setRotateState(RotateState.DRAG);
        setPivotState(PivotState.DRAG);
        leftRotate.setPosition(0.275);
        rightRotate.setPosition(0.275);
    }

    public void init() {
        rotateDegrees = 0;
        setGrabState(GrabState.CLOSED);
        setRotateState(RotateState.HOVER);
        setPivotState(PivotState.HOVER);
        pivot.setPosition(0.15);
    }

    public void start() {
        rotateDegrees = 0;
        setPivotState(PivotState.HOVER);
        setRotateState(RotateState.HOVER);
        setGrabState(GrabState.OPEN);
    }

    public void rotateCycleLeft() {
        rotateCycle(false);
    }

    public void rotateCycleRight() {
        rotateCycle(true);
    }

    public void telemetry() {
        telemetry.addData("Intake Grab State: ", grabState);
        telemetry.addData("Intake Rotate State: ", rotateState);
        telemetry.addData("Intake Pivot State: ", pivotState);
        telemetry.addData("Rotate Degrees: ", rotateDegrees);
    }

    public void periodic() {
        telemetry();
    }
}
