package config.core;

import androidx.annotation.NonNull;

import com.pedropathing.localization.Pose;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ManualPose {

    private Pose defaultPose;
    private double xOffset = 0; // Inches from the pink circle (x-direction)
    private double yOffset = 0; // Inches from the pink circle (y-direction)
    private double rotation = 0; // Degrees (counterclockwise positive)
    private double xTabs = 0, yTabs = 0;
    private Telemetry telemetry;

    public ManualPose(Telemetry t, boolean spec) {
        telemetry = t;
        if (spec)
            defaultPose = new Pose(23.5+22, 72, Math.toRadians(0));
        else
            defaultPose = new Pose(0, 0, 0);
    }

    public void f(double tabs) {
        xTabs += tabs;
    }

    public void b(double tabs) {
        xTabs -= tabs;
    }

    public void l(double tabs) {
        yTabs += tabs;
    }

    public void r(double tabs) {
        yTabs -= tabs;
    }

    public void t(boolean right) {
        if (right) {
            if (rotation < 90)
                rotation += 22.5;
        } else {
            if (rotation > -90)
                rotation -= 22.5;
        }
    }

    public Pose getPose() {
        calculate();
        return new Pose(xOffset + defaultPose.getX(), yOffset + defaultPose.getY(), defaultPose.getHeading());
    }

    public double getRotation() {
        return rotation;
    }

    public void reset() {
        xTabs = 0;
        yTabs = 0;
        xOffset = 0;
        yOffset = 0;
        rotation = 0;
    }

    public void calculate() {
        xOffset = getDistance(xTabs) * Math.signum(xTabs);
        yOffset = getDistance(yTabs) * Math.signum(yTabs);

        if (xTabs == 0) {
            xOffset = getDistance(5) * -1;
        }

        xOffset += Math.abs(rotation / 22.5) * 0.25;
    }

    public void update() {
        calculate();
        telemetry.addData("Tabs", "X: %.2f, Y: %.2f", xTabs, yTabs);
        telemetry.addData("Position", "X: %.2f inches, Y: %.2f inches", xOffset, yOffset);
        telemetry.addData("Rotation", "%.2f degrees", rotation);
    }

    @NonNull
    public String toString() {
        return "X Tabs: " + xTabs + ", Y Tabs: " + yTabs + "\n" + "X Offset: " + xOffset + ", Y Offset: " + yOffset + "\nRotation: " + rotation;
    }

    public double getDistance(double tiles) {
        double i = 0;
        switch ((int) tiles) {
            case 0:
                i = 0;
            case 1:
                i = 1.125;
            case 2:
                i = 2 + (double) 3/8;
            case 3:
                i = 3 + (double) 1/4;
            case 4:
                i = 5;
            case 5:
                i = 5 + (double) 3/4;
            case 6:
                i = 7 + (double) 3/8;
            case 7:
                i = 8 + (double) 1/4;
            case 8:
                i = 9 + (double) 5/8;
            case 9:
                i = 10 + (double) 1/2;
            case 10:
                i = 11 + (double) 3/8;
        }

        return i;
    }
}
