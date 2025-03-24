package opmode.auto;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.commands.FollowPath;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import config.commands.AlignSevenSpecFirst;
import config.commands.AlignSevenSpecSecond;
import config.commands.ForwardChamber;
import config.commands.SevenChamber;
import config.commands.SpecTransfer;
import config.commands.Specimen;
import config.commands.Submersible;
import config.commands.Transfer;
import config.core.util.Alliance;
import config.core.util.OpModeCommand;
import config.core.Robot;

@Autonomous(name = "7+0", group = "....Sigma")
public class SevenSpec extends OpModeCommand {
    Robot r;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, telemetry, Alliance.BLUE, config.core.paths.SevenSpec.start, true, 2);
        r.getI().init();
        r.getO().sevenInit();
        r.getO().close();
        r.getE().toZero();
        r.getT().addData("init", true);
        r.getT().addData("sub2", config.core.paths.SevenSpec.sub2);
        r.getT().addData("sub3", config.core.paths.SevenSpec.sub3);
        r.getT().update();

        schedule(
                new RunCommand(r::aPeriodic),
                new SequentialCommandGroup(
                        new ForwardChamber(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.SevenSpec.score1()),
                                        new WaitCommand(100)
                                                .andThen(
                                                        new InstantCommand(
                                                                () -> {
                                                                    r.getI().hover();
                                                                    r.getE().toFull();
                                                                }
                                                        )
                                                ),
                                        new WaitCommand(750)
                                                .andThen(
                                                        new InstantCommand(() -> r.getI().cloud())
                                                )
                                ),
                        new AlignSevenSpecFirst(r, r.getM().getManualPoses().get(0))
                                .andThen(
                                        new Submersible(r)

                                ),
                        new FollowPath(r.getF(), config.core.paths.SevenSpec.deposit2())
                                .alongWith(
                                        new SpecTransfer(r)
                                                .andThen(
                                                        new InstantCommand(
                                                                () -> {
                                                                    r.getO().specimenGrab0();
                                                                    r.getO().close();
                                                                }
                                                        )
                                                )
                                ),
                        new WaitCommand(200)
                                .andThen(
                                        new InstantCommand(() -> r.getO().open()),
                                        new FollowPath(r.getF(), config.core.paths.SevenSpec.grab2()).setCompletionThreshold(0.975)
                                                .andThen(new InstantCommand(() -> {
                                                    r.getO().close();
                                                    r.getI().init();
                                                }))
                                ),
                        new ForwardChamber(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.SevenSpec.score2()).setCompletionThreshold(0.975)
                                                .andThen(
                                                        new InstantCommand(
                                                                () -> {
                                                                    r.getI().hover();
                                                                    r.getE().toFull();
                                                                }
                                                        )
                                                )
                                ),
                        new AlignSevenSpecSecond(r, r.getM().getManualPoses().get(1))
                                .andThen(
                                        new Submersible(r),
                                        new InstantCommand(() -> r.getO().score())

                                ),
             /*           new FollowPath(r.getF(), config.core.paths.SevenSpec.deposit3())
                                .andThen(
                                        new InstantCommand(() -> r.getE().toFull()),
                                        new WaitCommand(200)
                                                .andThen(
                                                        new InstantCommand(() -> r.getI().open())
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.SevenSpec.push())
                                .alongWith(
                                        new WaitCommand(250).andThen(
                                            new InstantCommand(() -> r.getI().specimen()),
                                            new WaitCommand(500),
                                            new InstantCommand(() -> r.getE().toZero()),
                                            new InstantCommand(() -> r.getO().specimenGrab180())
                                        )
                                ),*/
                        new FollowPath(r.getF(), config.core.paths.SevenSpec.deposit3())
                                .alongWith(
                                        new InstantCommand(() -> r.getI().hover()),
                                        new WaitCommand(800)
                                                .andThen(
                                                        new InstantCommand(() -> r.getE().toFull())
                                                )
                                )
                                .andThen(new InstantCommand(() -> r.getI().open())),
                        new SequentialCommandGroup(
                                new FollowPath(r.getF(), config.core.paths.SevenSpec.push0())
                                        .alongWith(
                                                new WaitCommand(250)
                                                        .andThen(
                                                                new InstantCommand(() -> r.getI().drag()),
                                                                new InstantCommand(() -> r.getE().toQuarter())
                                                        )
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SevenSpec.push1())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toFull())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SevenSpec.push2())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toQuarter())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SevenSpec.push3())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toFull())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SevenSpec.push4())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toQuarter())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SevenSpec.push5())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toFull())
                                        ),
                                new InstantCommand(() -> r.getI().specimen()),
                                new WaitCommand(300),
                                new InstantCommand(() -> r.getE().toZero()),
                                new InstantCommand(() -> r.getO().specimenGrab180())
                        ),
                        new FollowPath(r.getF(), config.core.paths.SevenSpec.grab3()),
                        new SevenChamber(r)
                                .alongWith(
                                        new WaitCommand(250)
                                                .andThen(
                                                        new FollowPath(r.getF(), config.core.paths.SevenSpec.score3()).setCompletionThreshold(0.975)
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.SevenSpec.grab4(), true, 1)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SevenSpec.score4(), true, 1).setCompletionThreshold(0.975)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SevenSpec.grab5(), true, 1)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SevenSpec.score5(), true, 1).setCompletionThreshold(0.975)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SevenSpec.grab6(), true, 1)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SevenSpec.score6(), true, 1).setCompletionThreshold(0.975)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SevenSpec.grab7(), true, 1)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SevenSpec.score7(), true, 1).setCompletionThreshold(0.975)
                                        )
                        ),
                        //new FollowPath(r.getF(), config.core.paths.SevenSpec.park(), true, 1)
                        //        .alongWith(
                        new InstantCommand(
                                () -> {
                                    //                            r.getO().transfer();
                                    r.getI().hover();
                                }
                                //                ),
                                //                 new WaitCommand(500)
                                //                        .andThen(
                                //                                new InstantCommand(() -> r.getE().toFull())
                                //                        )
                        )
                )
        );
    }

    @Override
    public void init_loop() {
        super.init_loop();
       /* r.getM().update(gamepad2);

        if(gamepad2.left_stick_button) {
            r.getT().addLine();
            r.getT().addLine();

            config.core.paths.SevenSpec.sub2 = r.getM().getManualPoses().get(0).getPose().copy();
            config.core.paths.SevenSpec.sub3 = r.getM().getManualPoses().get(1).getPose().copy();

            r.getT().addData("sub2", config.core.paths.SevenSpec.sub2);
            r.getT().addData("sub3", config.core.paths.SevenSpec.sub3);

            r.getT().addLine();

            config.core.paths.SevenSpec.score1.setY(r.getM().getManualPoses().get(0).getPose().getY());
            config.core.paths.SevenSpec.score2.setY(r.getM().getManualPoses().get(1).getPose().getY());

            r.getT().addData("score1", config.core.paths.SevenSpec.score1);
            r.getT().addData("score2", config.core.paths.SevenSpec.score2);
        }
        r.getT().update();*/
    }
}
