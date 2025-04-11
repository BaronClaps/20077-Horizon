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
                if(timer.getElapsedTimeSeconds() > 0.1) {
                    robot.getO().halfScore(); }
                if (!robot.getF().isBusy() && robot.getL().halfwayToTarget() && timer.getElapsedTimeSeconds() > 0.25) {
                    setState(3); }
                break;
            case 3:
                if (timer.getElapsedTimeSeconds() > 0.25) {
                    robot.getO().score();
                    setState(4); }
                break;
            case 4:
                if (timer.getElapsedTimeSeconds() > 0.2) {
                    robot.getO().open();
                    setState(5); }
                break;
            case 5:
                if (timer.getElapsedTimeSeconds() > 0.4) {
                    robot.getL().toZero();
                    robot.getO().transfer();
                    robot.getI().hover();
                    setState(6); }
                break;
            case 6:
                if (timer.getElapsedTimeSeconds() > 0.3) {
                    setState(-1); }
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

