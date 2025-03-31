package config.core;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    // Outtake
    public static double outtakeGrabClose = 0;
    public static double outtakeGrabOpen = 0.4;
    public static double outtakeRotateTransfer = 0.575;
    public static double outtakeRotateSpecimenGrab180 = 0.64;
    public static double outtakeRotateSpecimenScore0 = 0.455; //.55
    public static double outtakeRotateLeftScore = 0.9;
    public static double outtakeRotateRightScore = 0.5;
    public static double outtakeRotateLeftSpecimenScore180 = 0.74;
    public static double outtakeRotateRightSpecimenScore180 = 0.3;
    public static double outtakeRotateLeftSpecimenGrab0 = 0.3;
    public static double outtakeRotateRightSpecimenGrab0 = 0.7;
    public static double outtakePivotTransfer= 0.85; //0
    public static double outtakePivotScore = 0.4;
    public static double outtakePivotSpecimenGrab180 = 0.9;
    public static double outtakePivotSpecimenScore180 = 0.36;

    public static double outtakePivotSpecimenGrab0 = 0.1;
    public static double outtakePivotSpecimenScore0 = 0.59;
    public static double outtakePivotAfterSpecScore = 0.25;
    public static double outtakePivotSpecimenScore0After = 0.75;
    public static double outtakeRotateSpecimenScore0After = 0.475; //.6

    // Intake
    public static double intakeGrabClose = 0.095;
    public static double intakeGrabOpen = 0.35;
    public static double intakeRotateTransfer = 0.3;
    public static double intakeRotateHoverVertical = 0.81;
    public static double intakeRotateGroundVertical = 0.77;
    public static double intakeRotateCloudVertical = 0.81;
    public static double intakeRotateSpecimen = 0.45;
    public static double intakePivotTransfer= 0.6;
    public static double intakePivotGround = 0.67;
    public static double intakePivotHover = 0.43;
    public static double intakePivotCloud = 0.6;
    public static double intakePivotSpecimen = 0;
    public static double intakeRotatePerDegree = 0.0011;
    public static double intakePivotDrag = 0.98;
    public static double intakeRotateDrag = 0.34;

    // Lift Positions
    public static int liftToZero = 10;
    public static int liftToHumanPlayer = 200;
    public static int liftToHighChamber = 200;
    public static int liftAfterHighChamber = 100;
    public static int liftToHighBucket = 2100;
    public static int liftToTransfer = 200;
    public static int liftToPark = 800;

    // Extend Positions
    public static double extendZero = 0.5075;
    public static double extendFull = 0.27;
    public static double extendTransfer = .53;
    public static double extendInchesPer = 0.01484375 * 16;

}