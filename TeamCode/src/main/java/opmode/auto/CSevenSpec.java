package opmode.auto;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.commands.FollowPath;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import config.commands.CAlignSevenSpecFirst;
import config.commands.CAlignSevenSpecSecond;
import config.commands.ForwardChamber;
import config.commands.SevenChamber;
import config.commands.SpecTransfer;
import config.commands.Specimen;
import config.commands.CSubmersible;
import config.commands.Transfer;
import config.core.util.Alliance;
import config.core.util.OpModeCommand;
import config.core.Robot;

@Autonomous(name = "C 7+0", group = "....Sigma")
public class CSevenSpec extends OpModeCommand {
    Robot r;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, telemetry, Alliance.BLUE, config.core.paths.CSevenSpec.start, true, 2);
        r.getI().init();
        r.getO().sevenInit();
        r.getO().close();
        r.getE().toZero();
        r.getJ().off();
        r.getT().addData("init", true);
        r.getT().addData("sub2", config.core.paths.CSevenSpec.sub2);
        r.getT().addData("sub3", config.core.paths.CSevenSpec.sub3);
        r.getT().update();

        schedule(
                new RunCommand(r::aPeriodic),
                new SequentialCommandGroup(
                        new ForwardChamber(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.score1()).setCompletionThreshold(0.95)
                                                .alongWith(
                                                        new WaitUntilCommand(() -> r.getF().getCurrentTValue() > 0.8)
                                                                .andThen(
                                                                        new InstantCommand(
                                                                                () -> {
                                                                                    r.getI().hover();
                                                                                    r.getE().toFull();
                                                                                }
                                                                        )
                                                                )
                                                )
                                ),
                        new CAlignSevenSpecFirst(r, r.getM().getManualPoses().get(0))
                                .andThen(
                                        new CSubmersible(r)
                                ),
                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.deposit2())
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
                        new WaitCommand(300)
                                .andThen(
                                        new InstantCommand(() -> r.getO().open()),
                                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.grab2()).setCompletionThreshold(0.975)
                                                .andThen(new InstantCommand(() -> {
                                                    r.getO().close();
                                                }))
                                ),
                        new ForwardChamber(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.score2()).setCompletionThreshold(0.95)
                                                                .andThen(
                                                                        new WaitCommand(250),
                                                                        new InstantCommand(
                                                                                () -> {
                                                                                    r.getI().hover();
                                                                                    r.getE().toFull();
                                                                                }
                                                                        )
                                                                )

                                ),
                        new CAlignSevenSpecSecond(r, r.getM().getManualPoses().get(1))
                                .andThen(
                                        new CSubmersible(r)

                                ),
             /*           new FollowPath(r.getF(), config.core.paths.CSevenSpec.deposit3())
                                .andThen(
                                        new InstantCommand(() -> r.getE().toFull()),
                                        new WaitCommand(200)
                                                .andThen(
                                                        new InstantCommand(() -> r.getI().open())
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.push())
                                .alongWith(
                                        new WaitCommand(250).andThen(
                                            new InstantCommand(() -> r.getI().specimen()),
                                            new WaitCommand(500),
                                            new InstantCommand(() -> r.getE().toZero()),
                                            new InstantCommand(() -> r.getO().specimenGrab180())
                                        )
                                ),*/
                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.deposit3())
                                .alongWith(
                                        new InstantCommand(() -> r.getI().hover()),
                                        new WaitCommand(800)
                                                .andThen(
                                                        new InstantCommand(() -> r.getE().toFull())
                                                )
                                )
                                .andThen(new InstantCommand(() -> r.getI().open())),
                        new SequentialCommandGroup(
                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.push0())
                                        .alongWith(
                                                new WaitCommand(250)
                                                        .andThen(
                                                                new InstantCommand(() -> r.getI().cDrag()),
                                                                new InstantCommand(() -> r.getE().toQuarter())
                                                        )
                                        ),
                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.push1())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toFull())
                                                        .andThen(
                                                                new WaitCommand(100),
                                                                new InstantCommand(() -> r.getE().toFull())
                                                        )
                                        ),
                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.push2())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toQuarter())
                                        ),
                                new InstantCommand(() -> r.getE().toFull())
                                        .andThen(
                                                new WaitCommand(200),
                                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.push3())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.push4())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toQuarter())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.push5())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toFull()),
                                                new WaitCommand(500)
                                                        .andThen(new InstantCommand(() -> r.getO().score()))
                                        ),
                                new InstantCommand(() -> r.getI().specimen()),
                                new WaitCommand(300),
                                new InstantCommand(() -> r.getE().toZero()),
                                new InstantCommand(() -> r.getO().specimenGrab180())
                        ),
                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.grab3()).setCompletionThreshold(0.99),
                        new SevenChamber(r)
                                .alongWith(
                                        new WaitCommand(250)
                                                .andThen(
                                                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.score3()).setCompletionThreshold(0.99)
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.grab4(), true, 1).setCompletionThreshold(0.99)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.score4(), true, 1).setCompletionThreshold(0.99)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.grab5(), true, 1).setCompletionThreshold(0.99)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.score5(), true, 1).setCompletionThreshold(0.99)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.grab6(), true, 1).setCompletionThreshold(0.99)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(250)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.score6(), true, 1).setCompletionThreshold(0.99)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.grab7(), true, 1).setCompletionThreshold(0.99)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.CSevenSpec.score7(), true, 1).setCompletionThreshold(0.99)
                                        )
                        ),
//                        new FollowPath(r.getF(), config.core.paths.CSevenSpec.park(), true, 1)
//                                .alongWith(
                        new InstantCommand(
                                () -> {
                                    //                            r.getO().transfer();
                                    r.getI().hover();
                                }
                                // )
                        )
                )
        );
    }

    @Override
    public void init_loop() {
        super.init_loop();
        r.getM().update(gamepad2);

        config.core.paths.CSevenSpec.score1 = new Pose(config.core.paths.CSevenSpec.score1.getX(), r.getM().getManualPoses().get(0).getPose().getY());
        config.core.paths.CSevenSpec.score2 = new Pose(config.core.paths.CSevenSpec.score2.getX(), r.getM().getManualPoses().get(1).getPose().getY());

        r.getT().addLine();
        r.getT().addData("sub2", config.core.paths.CSevenSpec.sub2);
        r.getT().addData("sub3", config.core.paths.CSevenSpec.sub3);
        r.getT().addLine();
        r.getT().addData("score1", config.core.paths.CSevenSpec.score1);
        r.getT().addData("score2", config.core.paths.CSevenSpec.score2);
        r.getT().addLine();
        r.getT().update();
    }
}
