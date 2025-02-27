package config.core.paths;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

public class FiveSpecOneSample {
    public static Pose start = new Pose(7, 78, Math.toRadians(180));
    public static Pose score1 = new Pose(40, 78, Math.toRadians(180));
    public static Pose grab2 = new Pose(8, 36, Math.toRadians(180));
    public static Pose score2 = new Pose(40, 75, Math.toRadians(180));
    public static Pose grab3 = new Pose(8, 36, Math.toRadians(180));
    public static Pose score3 = new Pose(40, 72, Math.toRadians(180));
    public static Pose grab4 = new Pose(8, 36, Math.toRadians(180));
    public static Pose score4 = new Pose(40, 69, Math.toRadians(180));
    public static Pose grab5 = new Pose(8, 36, Math.toRadians(180));
    public static Pose score5 = new Pose(40, 66, Math.toRadians(180));
    public static Pose grab6 = new Pose(8, 36, Math.toRadians(180));
    public static Pose score6 = new Pose(8, 123, Math.toRadians(270));
    public static Pose park = new Pose(9, 40, Math.toRadians(270));

    public static PathChain score1() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                start,
                                score1
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain push() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(score1),
                                new Point(25.500, 17.000, Point.CARTESIAN),
                                new Point(66.500, 44.000, Point.CARTESIAN),
                                new Point(55.000, 24.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                new Point(55.000, 24.000, Point.CARTESIAN),
                                new Point(18.000, 24.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierCurve(
                                new Point(18.000, 24.000, Point.CARTESIAN),
                                new Point(70.000, 24.000, Point.CARTESIAN),
                                new Point(55.000, 13.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                new Point(55.000, 13.000, Point.CARTESIAN),
                                new Point(18.000, 13.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierCurve(
                                new Point(18.000, 13.000, Point.CARTESIAN),
                                new Point(70.000, 24.000, Point.CARTESIAN),
                                new Point(55.000, 9, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                new Point(55.000, 9, Point.CARTESIAN),
                                new Point(18.000, 9, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain grab2() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(16, 9, Point.CARTESIAN),
                                new Point(36, 36, Point.CARTESIAN),
                                new Point(grab2)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain score2() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                grab2,
                                score2
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain grab3() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score2,
                                grab3
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain score3() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                grab3,
                                score3
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain grab4() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score3,
                                grab4
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain score4() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                grab4,
                                score4
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain grab5() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score4,
                                grab5
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain score5() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                grab5,
                                score5
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain grab6() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score5,
                                grab6
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain score6() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                grab6,
                                new Pose(32.000, 70.500),
                                score6
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
                .build();
    }

    public static PathChain park() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score6,
                                park
                        )
                )
                .setTangentHeadingInterpolation()
                .build();
    }
}
