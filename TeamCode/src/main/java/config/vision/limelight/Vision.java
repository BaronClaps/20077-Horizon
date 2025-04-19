package config.vision.limelight;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import config.vision.limelight.old.LL3ADetection;

@Config
public class Vision {
    // Limelight and claw configuration
    public static double limelightHeight = 19; // Camera height in inches
    public static double limelightAngle = 60; // Camera angle (0° = down, 90° = forward)
    public static double clawForwardOffset = 18; // Claw's forward offset from the camera
    public static double clawLateralOffset = 5; // Claw's lateral (right is +) offset from the camera

    private Pose sample = new Pose(), difference = new Pose(), target = new Pose(); // The best sample's position
    private Pose cachedTarget = new Pose(); // Cached best sample
    private Limelight3A limelight;
    private PathChain toTarget;
    private Telemetry telemetry;
    private int[] unwanted; // Color // Yellow = 0 // Blue = 1 // Red = 2
    private Follower f;

    public Vision(HardwareMap hardwareMap, Telemetry telemetry, int[] unwanted, Follower f) {
        this.unwanted = unwanted;
        this.telemetry = telemetry;
        this.f = f;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(5);
        limelight.start();
        f.update();
        cachedTarget = f.getPose();
        f.update();
    }

    public void find() {
            Sample.COLOR c = getSample().getColor();

            boolean colorMatch = true;

            int co = 0;

            if (c == Sample.COLOR.YELLOW)
                co = 0;

            if (c == Sample.COLOR.BLUE)
                co = 1;

            if (c == Sample.COLOR.RED)
                co = 2;

            for (int o : unwanted) {
                if (co == o) {
                    colorMatch = false;
                    break;
                }
            }

            if (colorMatch) {
                double xPixels = getSample().getX();
                double yPixels = getSample().getY();

                difference = new Pose(sample.getX() - clawForwardOffset, sample.getY() + clawLateralOffset, 0);

                target = new Pose(f.getPose().getX() + difference.getX(), f.getPose().getY() + difference.getY(), f.getPose().getHeading());

                cachedTarget = target.copy();

                toTarget = new PathBuilder()
                        .addPath(new BezierLine(f.getPose(), target)).setConstantHeadingInterpolation(f.getPose().getHeading()).build();

                //telemetry.addData("Best Detection", bestDetection.getDetection().getClassName());
                telemetry.addData("Sample Position", "X: %.2f, Y: %.2f", sample.getX(), sample.getY());
                telemetry.addData("diff", difference);
                telemetry.addData("target", target);
                telemetry.addData("current", f.getPose());
                //telemetry.addData("rotation", bestDetection.getAngle());
            } else {
                target = cachedTarget.copy();
            }
    }

    public Sample getSample() {
        LLResult result = limelight.getLatestResult();
        double[] Sample_data = result.getPythonOutput();
        telemetry.addData("Sample data", Sample_data);
        double color = Sample_data[0];
        double x = Sample_data[1];
        double y = Sample_data[2];
        double width = Sample_data[3];
        double height = Sample_data[4];
        double angle = Sample_data[5];
        if (color == 0) {
            return new Sample(Sample.COLOR.RED, x, y, angle);
        } else if (color == 1) {
            return new Sample(Sample.COLOR.BLUE, x, y, angle);
        } else if (color == 2) {
            return new Sample(Sample.COLOR.YELLOW, x, y, angle);
        }
        return null;
    }

    public PathChain toTarget() {
        toTarget = new PathBuilder()
                .addPath(new BezierLine(f.getPose(), target))//new Pose(target, target.getY())))
                .setConstantHeadingInterpolation(f.getPose().getHeading())
                .build();
        return toTarget;
    }

    public void off() {
        limelight.stop();
    }

    public void on() {
        limelight.start();
    }
}
