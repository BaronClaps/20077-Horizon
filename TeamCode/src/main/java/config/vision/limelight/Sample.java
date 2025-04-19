package config.vision.limelight;

import android.graphics.Color;

public class Sample {
    private COLOR color;
    private double x, y, angle;

    public Sample(COLOR color, double x, double y) {
        new Sample(color, x, y, 0);
    }

    public Sample(COLOR color, double x, double y, double angle) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public COLOR getColor() {
        return color;
    }

    public enum COLOR {
        RED, BLUE, YELLOW;

        Color min;
        Color max;

        public Color getMin() {
            return min;
        }

        public Color getMax() {
            return max;
        }
    }
}