package opmode.auto;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.commands.FollowPath;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import config.commands.Bucket;
import config.commands.Submersible;
import config.commands.Transfer;
import config.core.Robot;
import config.core.util.Alliance;
import config.core.util.OpModeCommand;

@Autonomous(name = "0+6", group = "...Unsigma")
public class FourSampDrag extends OpModeCommand {
    Robot r;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, telemetry, Alliance.BLUE, config.core.paths.FourSampDrag.start, false, 2);
        r.getI().init();
        r.getO().sevenInit();
        r.getO().close();
        r.getE().toZero();
        r.getT().addData("init", true);
        r.getT().addData("sub2", config.core.paths.FourSampDrag.sub2);
        r.getT().addData("sub3", config.core.paths.FourSampDrag.sub3);
        r.getT().update();

        schedule(
                new RunCommand(r::aPeriodic),
                new SequentialCommandGroup(
                        new Bucket(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.score1())
                                ),
                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.grab2())
                                .alongWith(
                                        new WaitCommand(700)
                                                .andThen(
                                                        new InstantCommand(() -> r.getE().toFull())
                                                                .andThen(
                                                                        new WaitCommand(450),
                                                                        new Submersible(r),
                                                                        new Transfer(r)
                                                                )
                                                )
                                ),
                        new Bucket(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.score2())
                                ),
                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.grab3())
                                .alongWith(
                                        new WaitCommand(500)
                                                .andThen(
                                                        new InstantCommand(() -> r.getE().toFull())
                                                                .andThen(
                                                                        new WaitCommand(450),
                                                                        new Submersible(r),
                                                                        new Transfer(r)
                                                                )
                                                )
                                ),
                        new Bucket(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.score3())
                                ),
                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.grab4())
                                .alongWith(
                                        new WaitCommand(500)
                                                .andThen(
                                                        new InstantCommand(() -> r.getE().toFull())
                                                                .andThen(
                                                                        new WaitCommand(450),
                                                                        new Submersible(r),
                                                                        new Transfer(r)
                                                                )
                                                )
                                ),
                        new Bucket(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.score4())
                                ),
       /*                 new AlignFourSampDragFirst(r, r.getM().getManualPoses().get(0))
                                .alongWith(
                                        new InstantCommand(() -> {
                                            r.getO().transfer();
                                            r.getI().hover();
                                        }
                                        )
                                                .andThen(
                                                        new WaitCommand(1500),
                                                        new InstantCommand(() -> r.getE().toFull()),
                                                        new WaitCommand(600),
                                                        new Submersible(r)
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.score3())
                                .alongWith(
                                        new WaitCommand(450),
                                        new Transfer(r)
                                ),
                        new Bucket(r)
                                .andThen(new AlignFourSampDragSecond(r, r.getM().getManualPoses().get(1))
                                        .alongWith(
                                                new InstantCommand(() -> {
                                                    r.getO().transfer();
                                                    r.getI().hover();
                                                }
                                                )
                                                        .andThen(
                                                                new WaitCommand(1500),
                                                                new InstantCommand(() -> r.getE().toFull()),
                                                                new WaitCommand(600),
                                                                new Submersible(r)
                                                        )
                                        ),
                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.score3())
                                                .alongWith(
                                                        new WaitCommand(450),
                                                        new Transfer(r)
                                                )
                                ),
                        new Bucket(r)*/
                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.drag(), 1)
                                .alongWith(
                                        new InstantCommand(() -> {
                                            r.getO().transfer();
                                            r.getI().hover();
                                            r.getL().toZero();
                                            r.getE().toZero();
                                        }
                                        )
                                ),

                        new FollowPath(r.getF(), config.core.paths.FourSampDrag.park(r.getF().getPose()), 1)
                                .alongWith(
                                        new InstantCommand(() -> {
                                            r.getO().transfer();
                                            r.getI().hover();
                                            r.getL().toPark();
                                            r.getE().toZero();
                                        }
                                        )
                                )
                )
        );
    }

    @Override
    public void init_loop() {
        super.init_loop();
        r.getM().update(gamepad2);

        r.getT().addLine();
        r.getT().addData("sub2", config.core.paths.FourSampDrag.sub2);
        r.getT().addData("sub3", config.core.paths.FourSampDrag.sub3);
        r.getT().addLine();
        r.getT().update();
    }
}