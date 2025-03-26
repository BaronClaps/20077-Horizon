package config.core.paths;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

import org.opencv.core.Mat;

public class SevenSpec {

    public static Pose start = new Pose(8, 65.5, Math.toRadians(0));
    public static Pose score1 = new Pose(43, 72 + (-3 * 1.181), Math.toRadians(0));
    public static Pose sub2 = new Pose(25 + (13 * 1.181), 72 + (-3 * 1.181), Math.toRadians(0)); // 48 + 2.5 - 18.5 - 7
    public static Pose deposit2 = new Pose(21,25.75, Math.toRadians(0));
    public static Pose grab2 = new Pose(17.5, 25.75, Math.toRadians(0));
    public static Pose score2 = new Pose(43, 72, Math.toRadians(0));
    public static Pose sub3 = new Pose(25 + (13* 1.181), 72, Math.toRadians(0)); // 48 + 2.5 - 18.5 - 7
    public static Pose deposit3 = new Pose(32,36, Math.toRadians(180));
    public static Pose deposit3Drag = new Pose(24, 48, Math.toRadians(245));
    public static Pose grab3Drag = new Pose(10, 36, Math.toRadians(180));
    public static Pose grab3 = new Pose(10, 36, Math.toRadians(180));
    public static Pose score3 = new Pose(39, 66, Math.toRadians(180));
    public static Pose grab4 = new Pose(10, 39, Math.toRadians(180));
    public static Pose score4 = new Pose(39, 66, Math.toRadians(180));
    public static Pose grab5 = new Pose(10, 39, Math.toRadians(180));
    public static Pose score5 = new Pose(39, 66, Math.toRadians(180));
    public static Pose grab6 = new Pose(10, 39, Math.toRadians(180));
    public static Pose score6 = new Pose(39, 66, Math.toRadians(180));
    public static Pose grab7 = new Pose(10, 39, Math.toRadians(180));
    public static Pose score7 = new Pose(39, 66, Math.toRadians(270));
    public static Pose park = new Pose(24,48, Math.toRadians(225));

    public static PathChain score1() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(start),
                                new Point(score1)
                        )
                )
                .setConstantHeadingInterpolation(start.getHeading())
                .setZeroPowerAccelerationMultiplier(4)
                .build();
    }

    public static PathChain sub2() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(score1),
                                new Point(sub2)
                        )
                )
                .setConstantHeadingInterpolation(score1.getHeading())
                .setZeroPowerAccelerationMultiplier(3)
                .build();

    }

    public static PathChain deposit2() {
        return new PathBuilder()
                .addPath(new BezierCurve(sub2, new Pose(sub2.getX() - 10, sub2.getY() + 10), deposit2))
                .setLinearHeadingInterpolation(sub2.getHeading(), deposit2.getHeading())
                .setZeroPowerAccelerationMultiplier(6)
                .build();
    }

    public static PathChain grab2() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                deposit2,
                                grab2
                        )
                )
                .setConstantHeadingInterpolation(deposit2.getHeading())
                .setZeroPowerAccelerationMultiplier(4)
                .build();
    }

    public static PathChain score2() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(grab2),
                                new Point(score2)
                        )
                )
                .setConstantHeadingInterpolation(grab2.getHeading())
                .setZeroPowerAccelerationMultiplier(4)
                .build();
    }

    public static PathChain sub3() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(score2),
                                new Point(sub3)
                        )
                )
                .setConstantHeadingInterpolation(score2.getHeading())
                .setZeroPowerAccelerationMultiplier(3)
                .build();

    }

    /*
    public static PathChain deposit3() {
        return new PathBuilder()
                .addPath(new BezierCurve(
                        sub3,
                        new Pose(sub3.getX() - 15, sub3.getY() + 10),
                        deposit3
                ))
                .setLinearHeadingInterpolation(
                        sub3.getHeading(),
                        deposit3.getHeading()
                )
                .setZeroPowerAccelerationMultiplier(6)
                .build();
    }*/

    public static PathChain deposit3() {
        return new PathBuilder()
                .addPath(new BezierCurve(sub3, new Pose(sub3.getX() - 10, sub3.getY() + 10), deposit3Drag))
                .setLinearHeadingInterpolation(sub3.getHeading(), deposit3Drag.getHeading())
                .setZeroPowerAccelerationMultiplier(8)
                .build();
    }

    public static PathChain push0() {
        return new PathBuilder()
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(deposit3Drag),
                                new Point(34.500, 47.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(deposit3Drag.getHeading(), Math.toRadians(305))
                .setZeroPowerAccelerationMultiplier(8)
                .build();
    }

    public static PathChain push1() {
        return new PathBuilder()
                .addPath(
                        // Line 2
                        new BezierLine(
                                new Point(34.500, 47.000, Point.CARTESIAN),
                                new Point(29.500, 45.500, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(305), Math.toRadians(260))
                .setZeroPowerAccelerationMultiplier(8)
                .build();

    }

    public static PathChain push2() {
        return new PathBuilder()
                .addPath(
                        // Line 3
                        new BezierLine(
                                new Point(29.500, 45.500, Point.CARTESIAN),
                                new Point(34.000, 36.500, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(260), Math.toRadians(305))
                .setZeroPowerAccelerationMultiplier(8)
                .build();
    }

    public static PathChain push3() {
        return new PathBuilder()
                .addPath(
                        // Line 4
                        new BezierLine(
                                new Point(34.000, 36.500, Point.CARTESIAN),
                                new Point(29.500, 36.50, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(305), Math.toRadians(250))
                .setZeroPowerAccelerationMultiplier(8)
                .build();
    }

    public static PathChain push4() {
        return new PathBuilder()
                .addPath(
                        // Line 5
                        new BezierLine(
                                new Point(29.500, 36.50, Point.CARTESIAN),
                                new Point(36.000, 25.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(250), Math.toRadians(315))
                .setZeroPowerAccelerationMultiplier(8)
                .build();
    }

    public static PathChain push5() {
        return new PathBuilder()
                .addPath(
                        // Line 6
                        new BezierLine(
                                new Point(36.000, 25.000, Point.CARTESIAN),
                                new Point(27.000, 29.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(315), Math.toRadians(250))
                .setZeroPowerAccelerationMultiplier(8)
                .build();
    }

    public static PathChain grab3() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(27, 29, Point.CARTESIAN),
                                new Point(grab3Drag.getX() + 10, grab3Drag.getY()),
                                new Point(grab3Drag)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(250), Math.toRadians(180))
                .setZeroPowerAccelerationMultiplier(4)
                .build();
    }

    public static PathChain score3() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                grab3Drag,
                                new Pose(score3.getX() - 10, score3.getY()),
                                score3
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setZeroPowerAccelerationMultiplier(8)
                .build();
    }

    /*
    public static PathChain push() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(deposit3),
                                new Point(70.000, 25.500, Point.CARTESIAN),
                                new Point(52.5, 25.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(deposit3.getHeading())
                .addPath(
                        new BezierLine(
                                new Point(52.5, 25.500, Point.CARTESIAN),
                                new Point(28.000, 25.500, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierCurve(
                                new Point(28.000, 25.500, Point.CARTESIAN),
                                new Point(70.000, 25.500, Point.CARTESIAN),
                                new Point(52.5, 15.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                new Point(52.5, 14.000, Point.CARTESIAN),
                                new Point(28.000, 15.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierCurve(
                                new Point(28.000, 15.000, Point.CARTESIAN),
                                new Point(65.000, 21.000, Point.CARTESIAN),
                                new Point(52.5, 9.5, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                new Point(52.5, 9.5, Point.CARTESIAN),
                                new Point(20, 9.5, Point.CARTESIAN)
                        )
                )
                .setZeroPowerAccelerationMultiplier(6)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain grab3() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(20, 9.5, Point.CARTESIAN),
                                new Point(grab3.getX() + 10, grab3.getY()),
                                new Point(grab3)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(250), Math.toRadians(180))
                .setZeroPowerAccelerationMultiplier(2)
                .build();
    }

    public static PathChain score3() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                grab3,
                                new Pose(score3.getX() - 10, score3.getY()),
                                score3
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setZeroPowerAccelerationMultiplier(6)
                .build();
    }*/

    public static PathChain grab4() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score3,
                                grab4
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setZeroPowerAccelerationMultiplier(4)
                .build();
    }

    public static PathChain score4() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                grab4,
                                new Pose(score4.getX() - 10, score4.getY()),
                                score4
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setZeroPowerAccelerationMultiplier(8)
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
                .setZeroPowerAccelerationMultiplier(4)
                .build();
    }

    public static PathChain score5() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                grab5,
                                new Pose(score5.getX() - 10, score5.getY()),
                                score5
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setZeroPowerAccelerationMultiplier(8)
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
                .setZeroPowerAccelerationMultiplier(4)
                .build();
    }

    public static PathChain score6() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                grab6,
                                new Pose(score6.getX() - 10, score6.getY()),
                                score6
                        )
                )
                .setZeroPowerAccelerationMultiplier(8)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain grab7() {
        return new PathBuilder()
                .addPath(
                        new BezierLine(
                                score6,
                                grab7
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setZeroPowerAccelerationMultiplier(4)
                .build();
    }

    public static PathChain score7() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                grab7,
                                new Pose(score7.getX() - 10, score7.getY()),
                                score7
                        )
                )
                .setZeroPowerAccelerationMultiplier(8)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public static PathChain park() {
        return new PathBuilder()
                .addPath(
                        new BezierCurve(
                                score7,
                                new Pose(score7.getX() - 10, score7.getY()),
                                park
                        )
                )
                .setLinearHeadingInterpolation(score7.getHeading(), park.getHeading())
                .setZeroPowerAccelerationMultiplier(7)
                .build();
    }
}


