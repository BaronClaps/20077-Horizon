package indubitables.config.core;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    // Outtake
    public static double outtakeGrabClose = 0.04;
    public static double outtakeGrabOpen = 0.2;
    public static double outtakeRotateTransfer = 0.265; //.775
    public static double outtakeRotateSpecimenGrab = 0.73;
    public static double outtakeRotateLeftScore = 0.83;
    public static double outtakeRotateRightScore = 0.4375;
    public static double outtakeRotateLeftSpecimenScore = 0.815;
    public static double outtakeRotateRightSpecimenScore = 0.35;
    public static double outtakePivotTransfer= 0.18; //0
    public static double outtakePivotScore = 0.475;
    public static double outtakePivotSpecimenGrab = 0.92;
    public static double outtakePivotSpecimenScore = 0.375;

    // Intake
    public static double intakeGrabClose = 0.12;
    public static double intakeGrabOpen = 0.35;
    public static double intakeRotateTransfer = 0.355;
    public static double intakeRotateHoverVertical = 0.3;
    public static double intakeRotateGroundVertical = 0.38;
    public static double intakeRotateSpecimen = 0.9;
    public static double intakePivotTransfer= 0;
    public static double intakePivotGround = 0.64;
    public static double intakePivotHover = 0.4;
    public static double intakePivotSpecimen = 0;

    // Lift Positions
    public static int liftToZero = 0;
    public static int liftToHumanPlayer = 200;
    public static int liftToHighChamber = 200;
    public static int liftToHighBucket = 1750;
    public static int liftToTransfer = 200;
    public static int liftToPark = 0;

    // Extend Positions
    public static double extendZero = 0;
    public static double extendFull = 0.525;

}