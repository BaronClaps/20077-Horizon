package config.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.util.Timer;

import config.core.Robot;

public class Submersible extends CommandBase {
    private final Robot robot;

    private int state = 0;
    private Timer timer = new Timer();

    public Submersible(Robot robot) {
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
                robot.getI().cloud();
                robot.getI().open();
                robot.getO().transfer();
                setState(2);
                break;
            case 2:
                if(timer.getElapsedTimeSeconds() > 0.25) {
                    robot.getI().ground();
                    robot.getI().close();
                    setState(3);
                }
                break;
            case 3:
                if (timer.getElapsedTimeSeconds() > 0.2) {
                    robot.getI().hover();
                    setState(4);
                }
                break;
            case 4:
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

