package config.core.paths;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;

public class FourSamp {
    public static Pose start = new Pose(6.75, 114, Math.toRadians(270));
    public static Pose score = new Pose(15.25, 128.25, Math.toRadians(-45));
    public static Pose second = new Pose (19, 127.5, Math.toRadians(345.5));
    public static Pose third = new Pose(18.75, 130, Math.toRadians(4));
    public static Pose fourth = new Pose(20.5, 133, Math.toRadians(20.25));
    public static Pose park = new Pose(57.25, 100, Math.toRadians(270));
    public static PathChain score1() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                start, score
                        )
                )
                .setLinearHeadingInterpolation(start.getHeading(), score.getHeading())
                .build();
    }

    public static PathChain grab2() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score, second
                        )
                )
                .setLinearHeadingInterpolation(score.getHeading(), second.getHeading())
                .build();
    }

    public static PathChain score2() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                second, score
                        )
                )
                .setLinearHeadingInterpolation(second.getHeading(), score.getHeading())
                .build();
    }

    public static PathChain grab3() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score, third
                        )
                )
                .setLinearHeadingInterpolation(score.getHeading(), third.getHeading())
                .build();
    }

    public static PathChain score3() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                third, score
                        )
                )
                .setLinearHeadingInterpolation(third.getHeading(), score.getHeading())
                .build();
    }

    public static PathChain grab4() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score, fourth
                        )
                )
                .setLinearHeadingInterpolation(score.getHeading(), fourth.getHeading())
                .build();
    }

    public static PathChain score4() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                fourth, score
                        )
                )
                .setLinearHeadingInterpolation(fourth.getHeading(), score.getHeading())
                .build();
    }

    public static PathChain park() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                score, new Pose(park.getX(), park.getY() + 20), park
                        )
                )
                .setLinearHeadingInterpolation(score.getHeading(), park.getHeading())
                .build();
    }

}
