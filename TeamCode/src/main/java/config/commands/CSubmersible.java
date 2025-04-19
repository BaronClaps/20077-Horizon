package config.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.util.Timer;

import config.core.Robot;
import config.subsystems.Intake;

public class CSubmersible extends CommandBase {
    private final Robot robot;

    private int state = 0;
    private Timer timer = new Timer();
    private boolean alrCloud = false;

    public CSubmersible(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void initialize() {
        setState(1);
    }

    @Override
    public void execute() {
        robot.getT().addData("s state", state);
        robot.getT().update();
        switch (state) {
            case 1:
                if(!(robot.getI().pivotState == Intake.PivotState.CLOUD)) {
                    robot.getI().open();
                    alrCloud = false;
                    setState(2);
                } else {
                    alrCloud = true;
                    setState(2);
                }

                break;
            case 2:
                robot.getI().ground();
                setState(3);
                break;
            case 3:
                if (timer.getElapsedTimeSeconds() > 0.15) {
                    robot.getI().close();
                    setState(4);
                }
                break;
            case 4:
                if (timer.getElapsedTimeSeconds() > 0.2) {
                    robot.getI().hover();
                    setState(5);
                }
                break;
            case 5:
                if (timer.getElapsedTimeSeconds() > 0.1) {
                    robot.getE().toZero();
                    setState(-1);
                }
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

