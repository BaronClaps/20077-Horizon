package config.subsystems;

import static config.core.RobotConstants.*;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.pedropathing.util.Timer;


/** @author Baron Henderson
 * @version 2.0 | 1/4/25
 */

public class Outtake {

    public enum GrabState {
        CLOSED, OPEN
    }

    public enum RotateState {
        TRANSFER, SCORE, SPECIMENGRAB180, SPECIMENSCORE180, SPECIMENGRAB0, SPECIMENSCORE0
    }
    
    public enum PivotState {
        TRANSFER, SCORE, SPECIMENGRAB180, SPECIMENSCORE180, SPECIMENGRAB0, SPECIMENSCORE0
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
            leftRotate.setPosition(outtakeRotateTransfer+0.045);
            rightRotate.setPosition(outtakeRotateTransfer);
            this.rotateState = RotateState.TRANSFER;
        } else if (state == RotateState.SCORE) {
            leftRotate.setPosition(outtakeRotateLeftScore+0.045);
            rightRotate.setPosition(outtakeRotateRightScore);
            this.rotateState = RotateState.SCORE;
        } else if (state == RotateState.SPECIMENGRAB180) {
            leftRotate.setPosition(outtakeRotateSpecimenGrab180+0.045);
            rightRotate.setPosition(outtakeRotateSpecimenGrab180);
            this.rotateState = RotateState.SPECIMENGRAB180;
        } else if (state == RotateState.SPECIMENSCORE180) {
            leftRotate.setPosition(outtakeRotateLeftSpecimenScore180);
            rightRotate.setPosition(outtakeRotateRightSpecimenScore180);
            this.rotateState = RotateState.SPECIMENSCORE180;
        } else if (state == RotateState.SPECIMENGRAB0) {
            leftRotate.setPosition(outtakeRotateLeftSpecimenGrab0+0.045);
            rightRotate.setPosition(outtakeRotateRightSpecimenGrab0);
            this.rotateState = RotateState.SPECIMENGRAB0;
        } else if (state == RotateState.SPECIMENSCORE0) {
            leftRotate.setPosition(outtakeRotateSpecimenScore0+0.045);
            rightRotate.setPosition(outtakeRotateSpecimenScore0);
            this.rotateState = RotateState.SPECIMENSCORE0;
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
        } else if (pivotState == PivotState.SPECIMENGRAB180) {
            leftPivot.setPosition(outtakePivotSpecimenGrab180);
            rightPivot.setPosition(outtakePivotSpecimenGrab180);
            this.pivotState = PivotState.SPECIMENGRAB180;
        } else if (pivotState == PivotState.SPECIMENSCORE180) {
            leftPivot.setPosition(outtakePivotSpecimenScore180);
            rightPivot.setPosition(outtakePivotSpecimenScore180);
            this.pivotState = PivotState.SPECIMENSCORE180;
        } else if (pivotState == PivotState.SPECIMENGRAB0) {
            leftPivot.setPosition(outtakePivotSpecimenGrab0);
            rightPivot.setPosition(outtakePivotSpecimenGrab0);
            this.pivotState = PivotState.SPECIMENGRAB0;
        } else if (pivotState == PivotState.SPECIMENSCORE0) {
            leftPivot.setPosition(outtakePivotSpecimenScore0);
            rightPivot.setPosition(outtakePivotSpecimenScore0);
            this.pivotState = PivotState.SPECIMENSCORE0;
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

    public void specimenGrab180() {
        setRotateState(RotateState.SPECIMENGRAB180);
        setPivotState(PivotState.SPECIMENGRAB180);
        setGrabState(GrabState.OPEN);
    }

    public void specGrab() {
        switch(specGrabState) {
            case 0:
                if(pivotState == PivotState.SPECIMENSCORE180) {
                    afterSpecScore();
                    setSpecGrabState(1);
                } else {
                    setRotateState(RotateState.SPECIMENGRAB180);
                    setPivotState(PivotState.SPECIMENGRAB180);
                    setGrabState(GrabState.OPEN);
                    setSpecGrabState(-1);
                }
                break;
            case 1:
                if(specScoreTimer.getElapsedTimeSeconds() > 0.2) {
                    setGrabState(GrabState.OPEN);
                    setSpecGrabState(2);
                }
                break;
            case 2:
                if(specScoreTimer.getElapsedTimeSeconds() > 0.45) {
                    setRotateState(RotateState.SPECIMENGRAB180);
                    setPivotState(PivotState.SPECIMENGRAB180);
                    setSpecGrabState(-1);
                }
                break;
        }
    }

    public void setSpecGrabState(int i) {
        specGrabState = i;
        specScoreTimer.resetTimer();
    }

    public void startSpecGrab() {
        setSpecGrabState(0);
    }

    public void specGrabAuto() {
        switch(specGrabState) {
            case 0:
                if(pivotState == PivotState.SPECIMENSCORE180) {
                    afterSpecScore();
                    setSpecGrabState(1);
                } else {
                    setRotateState(RotateState.SPECIMENGRAB180);
                    setPivotState(PivotState.SPECIMENGRAB180);
                    setGrabState(GrabState.OPEN);
                    setSpecGrabState(-1);
                }
                break;
            case 1:
                if(specScoreTimer.getElapsedTimeSeconds() > 0.2) {
                    setGrabState(GrabState.OPEN);
                    setSpecGrabState(2);
                }
                break;
            case 2:
                if(specScoreTimer.getElapsedTimeSeconds() > 0.25) {
                    setRotateState(RotateState.SPECIMENGRAB180);
                    setPivotState(PivotState.SPECIMENGRAB180);
                    setSpecGrabState(-1);
                }
                break;
        }
    }

    public void setSpecGrabAutoState(int i) {
        specGrabState = i;
        specScoreTimer.resetTimer();
    }

    public void startSpecGrabAuto() {
        setSpecGrabAutoState(0);
    }

    public void preload() {
        setRotateState(RotateState.SPECIMENSCORE180);
        setPivotState(PivotState.SPECIMENSCORE180);
        leftPivot.setPosition(.37);
        rightPivot.setPosition(.37);
        setGrabState(GrabState.CLOSED);
    }


    public void specimenScore180() {
        setRotateState(RotateState.SPECIMENSCORE180);
        setPivotState(PivotState.SPECIMENSCORE180);
        setGrabState(GrabState.CLOSED);
    }

    public void specimenGrab0() {
        setRotateState(RotateState.SPECIMENGRAB0);
        setPivotState(PivotState.SPECIMENGRAB0);
        setGrabState(GrabState.OPEN);
    }

    public void specimenScore0() {
        setRotateState(RotateState.SPECIMENSCORE0);
        setPivotState(PivotState.SPECIMENSCORE0);
        setGrabState(GrabState.CLOSED);
    }

    public void init() {
        setPivotState(PivotState.SPECIMENGRAB180);
        leftPivot.setPosition(outtakePivotSpecimenGrab180-0.085);
        rightPivot.setPosition(outtakePivotSpecimenGrab180-0.085);
        setRotateState(RotateState.SPECIMENSCORE180);
        leftRotate.setPosition(0.74+0.21);
        rightRotate.setPosition(0.32+0.21);
        setGrabState(GrabState.CLOSED);
    }

    public void sevenInit() {
        setRotateState(RotateState.SPECIMENSCORE0);
        setPivotState(PivotState.SPECIMENSCORE0);
        leftPivot.setPosition(outtakePivotSpecimenScore0+0.075);
        rightPivot.setPosition(outtakePivotSpecimenScore0+0.075);
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
        setRotateState(RotateState.SPECIMENSCORE180);
        leftPivot.setPosition(0.15);
        rightPivot.setPosition(0.15);
        setGrabState(GrabState.CLOSED);
    }

    public void afterSpecScore() {
        setRotateState(RotateState.SPECIMENSCORE180);
        leftPivot.setPosition(outtakePivotAfterSpecScore);
        rightPivot.setPosition(outtakePivotAfterSpecScore);
        setGrabState(GrabState.CLOSED);
    }

    public void telemetry() {
        telemetry.addData("Outtake Grab State: ", grabState);
        telemetry.addData("Outtake Rotate State: ", rotateState);
        telemetry.addData("Outtake Pivot State: ", pivotState);
    }

    public void periodic() {
        specGrabAuto();
        specGrab();
        telemetry();
    }
}
