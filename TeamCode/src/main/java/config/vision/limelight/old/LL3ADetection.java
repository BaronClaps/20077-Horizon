package config.vision.limelight.old;

import com.qualcomm.hardware.limelightvision.LLResultTypes;

public class LL3ADetection {
    private final LLResultTypes.DetectorResult detection;
    private final double score;
    private final double yDistance;
    private final double rotationScore;
    private final double xDistance;
    private final double angle;

    public LL3ADetection(LLResultTypes.DetectorResult detection, double score, double yDistance, double xDistance, double rotationScore, double angle) {
        this.detection = detection;
        this.score = score;
        this.yDistance = yDistance;
        this.rotationScore = rotationScore;
        this.xDistance = xDistance;
        this.angle = angle;
    }

    public LLResultTypes.DetectorResult getDetection() {
        return detection;
    }
    public double getScore() {
        return score;
    }
    public double getYDistance() {
        return yDistance;
    }
    public double getXDistance() {
        return xDistance;
    }
    public double getRotationScore() {
        return rotationScore;
    }

    public double getAngle() {
        return angle;
    }
}