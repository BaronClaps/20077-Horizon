package config.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.util.Timer;

import config.core.Robot;

public class Bucket extends CommandBase {
    private final Robot robot;

    private int state = 0;
    private Timer timer = new Timer();

    public Bucket(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void initialize() {
        setState(1);
    }

    @Override
    public void execute() {
        switch (state) {
            case 1:
                robot.getL().toHighBucket();
                robot.getO().close();
                robot.getE().toZero();
                setState(2);
                break;
            case 2:
                if (!robot.getF().isBusy() && robot.getL().roughlyAtTarget() && timer.getElapsedTimeSeconds() > 0.25) {
                    setState(3);
                }
                break;
            case 3:
                if (timer.getElapsedTimeSeconds() > 0.1) {
                    robot.getO().score();
                    setState(4);
                }
                break;
            case 4:
                if (timer.getElapsedTimeSeconds() > 0.45) {
                    robot.getO().open();
                    setState(5);
                }
            case 5:
                if (timer.getElapsedTimeSeconds() > 0.65) {
                    robot.getL().toZero();
                    robot.getO().transfer();
                    setState(6);
                }
                break;
            case 6:
                if (timer.getElapsedTimeSeconds() > 0.5 && robot.getL().getPos() < 700) {
                    robot.getI().hover();
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

