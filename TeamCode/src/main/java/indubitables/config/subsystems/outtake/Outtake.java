package indubitables.config.subsystems.outtake;

import static indubitables.config.core.RobotConstants.*;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.pedropathing.util.Timer;


/** @author Baron Henderson
 * @version 2.0 | 1/4/25
 */

public class Outtake extends SubsystemBase {

    public enum GrabState {
        CLOSED, OPEN
    }

    public enum RotateState {
        TRANSFER, SCORE, SPECIMENGRAB, SPECIMENSCORE
    }
    
    public enum PivotState {
        TRANSFER, SCORE, SPECIMENGRAB, SPECIMENSCORE
    }

    public Servo grab, leftRotate, rightRotate, leftPivot, rightPivot;
    public GrabState grabState;
    public RotateState rotateState;
    public PivotState pivotState;
    private Telemetry telemetry;
    private Timer specScoreTimer = new Timer();
    private int specGrabState = -1;

    public Outtake(HardwareMap hardwareMap, Telemetry telemetry) {
        grab = hardwareMap.get(Servo.class, "oG");
        leftRotate = hardwareMap.get(Servo.class, "oLR");
        rightRotate = hardwareMap.get(Servo.class, "oRR");
        leftPivot = hardwareMap.get(Servo.class, "oLP");
        rightPivot = hardwareMap.get(Servo.class, "oRP");
        this.telemetry = telemetry;
    }

    public void setRotateState(RotateState state) {
        if (state == RotateState.TRANSFER) {
            leftRotate.setPosition(outtakeRotateTransfer);
            rightRotate.setPosition(outtakeRotateTransfer);
            this.rotateState = RotateState.TRANSFER;
        } else if (state == RotateState.SCORE) {
            leftRotate.setPosition(outtakeRotateLeftScore);
            rightRotate.setPosition(outtakeRotateRightScore);
            this.rotateState = RotateState.SCORE;
        } else if (state == RotateState.SPECIMENGRAB) {
            leftRotate.setPosition(outtakeRotateSpecimenGrab);
            rightRotate.setPosition(outtakeRotateSpecimenGrab);
            this.rotateState = RotateState.SPECIMENGRAB;
        } else if (state == RotateState.SPECIMENSCORE) {
            leftRotate.setPosition(outtakeRotateLeftSpecimenScore);
            rightRotate.setPosition(outtakeRotateRightSpecimenScore);
            this.rotateState = RotateState.SPECIMENSCORE;
        }
    }

    public void setGrabState(GrabState grabState) {
        if (grabState == GrabState.CLOSED) {
            grab.setPosition(outtakeGrabClose);
            this.grabState = GrabState.CLOSED;
        } else if (grabState == GrabState.OPEN) {
            grab.setPosition(outtakeGrabOpen);
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
            leftPivot.setPosition(outtakePivotTransfer);
            rightPivot.setPosition(outtakePivotTransfer);
            this.pivotState = PivotState.TRANSFER;
        } else if (pivotState == PivotState.SCORE) {
            leftPivot.setPosition(outtakePivotScore);
            rightPivot.setPosition(outtakePivotScore);
            this.pivotState = PivotState.SCORE;
        } else if (pivotState == PivotState.SPECIMENGRAB) {
            leftPivot.setPosition(outtakePivotSpecimenGrab);
            rightPivot.setPosition(outtakePivotSpecimenGrab);
            this.pivotState = PivotState.SPECIMENGRAB;
        } else if (pivotState == PivotState.SPECIMENSCORE) {
            leftPivot.setPosition(outtakePivotSpecimenScore);
            rightPivot.setPosition(outtakePivotSpecimenScore);
            this.pivotState = PivotState.SPECIMENSCORE;
        }
    }

    public void open() {
        setGrabState(GrabState.OPEN);
    }

    public void close() {
        setGrabState(GrabState.CLOSED);
    }

    public void transferHigh() {
        leftPivot.setPosition(outtakePivotTransfer+0.1);
        rightPivot.setPosition(outtakePivotTransfer+0.1);
    }

    public void transfer() {
        setRotateState(RotateState.TRANSFER);
        setPivotState(PivotState.TRANSFER);
        setGrabState(GrabState.OPEN);
    }

    public void score() {
        setRotateState(RotateState.SCORE);
        setPivotState(PivotState.SCORE);
        setGrabState(GrabState.CLOSED);
    }

    public void specimenGrab() {
        setRotateState(RotateState.SPECIMENGRAB);
        setPivotState(PivotState.SPECIMENGRAB);
        setGrabState(GrabState.OPEN);
    }

    public void specGrab() {
        switch(specGrabState) {
            case 0:
                if(pivotState == PivotState.SPECIMENSCORE) {
                    hang();
                    setGrabState(GrabState.OPEN);
                    setSpecGrabState(1);
                } else {
                    setRotateState(RotateState.SPECIMENGRAB);
                    setPivotState(PivotState.SPECIMENGRAB);
                    setGrabState(GrabState.OPEN);
                    setSpecGrabState(-1);
                }
                break;
            case 1:
                if(specScoreTimer.getElapsedTimeSeconds() > 0.65) {
                    setRotateState(RotateState.SPECIMENGRAB);
                    setPivotState(PivotState.SPECIMENGRAB);
                    setGrabState(GrabState.OPEN);
                    setSpecGrabState(-1);
                }
        }
    }

    public void setSpecGrabState(int i) {
        specGrabState = i;
        specScoreTimer.resetTimer();
    }

    public void startSpecGrab() {
        setSpecGrabState(0);
    }

    public void specimenScore() {
        setRotateState(RotateState.SPECIMENSCORE);
        setPivotState(PivotState.SPECIMENSCORE);
        setGrabState(GrabState.CLOSED);
    } 

    public void init() {
        leftPivot.setPosition(0.175);
        rightPivot.setPosition(0.175);
        setRotateState(RotateState.TRANSFER);
        setGrabState(GrabState.CLOSED);
    }

    public void start() {
        setPivotState(PivotState.TRANSFER);
        setRotateState(RotateState.TRANSFER);
        setGrabState(GrabState.CLOSED);
    }

    public void loop() {
        specGrab();
    }

    public void hang() {
        setRotateState(RotateState.SPECIMENSCORE);
        leftPivot.setPosition(0.7); //.7
        rightPivot.setPosition(0.7); //.7
        setGrabState(GrabState.CLOSED);
    }

    public void telemetry() {
        telemetry.addData("Outtake Grab State: ", grabState);
        telemetry.addData("Outtake Rotate State: ", rotateState);
        telemetry.addData("Outtake Pivot State: ", pivotState);
    }

    @Override
    public void periodic() {
        telemetry();
    }
}