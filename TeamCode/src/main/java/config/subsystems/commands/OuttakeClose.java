package config.subsystems.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import config.core.Robot;

public class OuttakeClose extends CommandBase {
    private final Robot robot;

    public OuttakeClose(Robot robot) {
        this.robot = robot;

    }

    @Override
    public void initialize() {
        robot.getO().close();
    }
}
