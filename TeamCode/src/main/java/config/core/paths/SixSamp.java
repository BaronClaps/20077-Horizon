package config.core.paths;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;

public class SixSamp {
    public static Pose start = new Pose(6.75, 114, Math.toRadians(270));
    public static Pose score = new Pose(19.5, 128.5, Math.toRadians(-45));
    public static Pose second = new Pose (19, 124.25, Math.toRadians(0));
    public static Pose third = new Pose(18.75, 131, Math.toRadians(0));
    public static Pose fourth = new Pose(20, 133, Math.toRadians(20.25));
    public static Pose sub2 = new Pose(63, 94, Math.toRadians(-90));
    public static Pose subControlPoint = new Pose(66.40214477211796, 111.95710455764075);
    public static Pose sub3 = new Pose(67, 92, Math.toRadians(-90));
    public static Pose park = new Pose(57.25, 96, Math.toRadians(270));
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

    public static PathChain sub2() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                score, subControlPoint, sub2
                        )
                )
                .setLinearHeadingInterpolation(score.getHeading(), sub2.getHeading())
                .setZeroPowerAccelerationMultiplier(1.5)
                .build();
    }

    public static PathChain score5() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                sub2, score
                        )
                )
                .setLinearHeadingInterpolation(sub2.getHeading(), score.getHeading())
                .setZeroPowerAccelerationMultiplier(1.5)
                .build();
    }

    public static PathChain sub3() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                score, subControlPoint, sub3
                        )
                )
                .setLinearHeadingInterpolation(score.getHeading(), sub3.getHeading())
                .setZeroPowerAccelerationMultiplier(1.5)
                .build();
    }

    public static PathChain score6() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                sub3, score
                        )
                )
                .setLinearHeadingInterpolation(sub3.getHeading(), score.getHeading())
                .setZeroPowerAccelerationMultiplier(1.5)
                .build();
    }
}
