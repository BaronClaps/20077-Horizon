package config.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.util.Timer;

import config.core.ManualPose;
import config.core.Robot;
import config.core.paths.SevenSpec;
import config.core.paths.SixSpecPush;

public class AlignSixSpecPush extends CommandBase {
    private final Robot robot;

    private int state = 0;
    private Timer timer = new Timer();
    private ManualPose manualPose;

    public AlignSixSpecPush(Robot robot, ManualPose manualPose) {
        this.robot = robot;
        this.manualPose = manualPose;
    }

    @Override
    public void initialize() {
        setState(1);
    }

    @Override
    public void execute() {
        robot.getT().addData("a state", state);
        robot.getT().update();
        switch (state) {
            case 1://robot.getV().getBestDetectionAngle());
                SixSpecPush.sub2 = manualPose.getPose().copy();
                robot.getF().followPath(SixSpecPush.sub2());
                setState(2);
                break;
            case 2:
                if (timer.getElapsedTimeSeconds() > 0.2) {
                    //robot.getI().cloud();
                    setState(3);
                }
                break;
            case 3:
                //   if(robot.getF().getPose().roughlyEquals(SixSpec.sub1, 0.25)) {
                if(timer.getElapsedTimeSeconds() > 0.2) {
                    robot.getI().rotateDegrees = manualPose.getRotation();
                    setState(-1);
                }
                //   }
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return state == -1;
    }

    public void setState(int x) {
        state = x;
        timer.resetTimer();
    }

}