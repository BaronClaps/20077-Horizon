package opmode.auto;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.commands.FollowPath;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import config.commands.AlignSixSpecOneSample;
import config.commands.ForwardChamber;
import config.commands.SevenChamber;
import config.commands.SpecTransfer;
import config.commands.Specimen;
import config.commands.Submersible;
import config.commands.Transfer;
import config.core.util.Alliance;
import config.core.util.OpModeCommand;
import config.core.Robot;
import config.subsystems.Outtake;

@Autonomous(name = "6+1", group = "....Sigma")
public class SixSpecOneSample extends OpModeCommand {
    Robot r;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, telemetry, Alliance.BLUE, config.core.paths.SixSpecOneSample.start, true, 1);
        r.getI().init();
        r.getO().sevenInit();
        r.getO().close();
        r.getE().toZero();
        r.getT().addData("init", true);
        r.getT().addData("sub2", config.core.paths.SixSpecOneSample.sub2);
        r.getT().update();

        schedule(
                new RunCommand(r::aPeriodic),
                new SequentialCommandGroup(
                        new ForwardChamber(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.score1()).setCompletionThreshold(0.975)
                                                .andThen(
                                                        new InstantCommand(() -> { r.getE().toFull(); })
                                                ),
                                        new WaitCommand(1000)
                                                .andThen(new InstantCommand(() -> { r.getI().cloud(); }))
                                ),
                        new AlignSixSpecOneSample(r, r.getM().getManualPoses().get(0))
                                .andThen(
                                        new Submersible(r)

                                ),
             /*           new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.deposit3())
                                .andThen(
                                        new InstantCommand(() -> r.getE().toFull()),
                                        new WaitCommand(200)
                                                .andThen(
                                                        new InstantCommand(() -> r.getI().open())
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.push())
                                .alongWith(
                                        new WaitCommand(250).andThen(
                                            new InstantCommand(() -> r.getI().specimen()),
                                            new WaitCommand(500),
                                            new InstantCommand(() -> r.getE().toZero()),
                                            new InstantCommand(() -> r.getO().specimenGrab180())
                                        )
                                ),*/
                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.deposit2())
                                .alongWith(
                                        new InstantCommand(() -> r.getI().hover()),
                                        new WaitCommand(800)
                                                .andThen(
                                                        new InstantCommand(() -> r.getE().toFull())
                                                )
                                )
                                .andThen(new InstantCommand(() -> r.getI().open())),
                        new SequentialCommandGroup(
                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.push0())
                                        .alongWith(
                                                new WaitCommand(250)
                                                        .andThen(
                                                                new InstantCommand(() -> r.getI().drag()),
                                                                new InstantCommand(() -> r.getE().toQuarter())
                                                        )
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.push1())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toFull())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.push2())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toQuarter())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.push3())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toFull())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.push4())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toQuarter())
                                        ),
                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.push5())
                                        .alongWith(
                                                new InstantCommand(() -> r.getE().toFull())
                                        ),
                                new InstantCommand(() -> r.getI().specimen()),
                                new WaitCommand(500),
                                new InstantCommand(() -> r.getE().toZero()),
                                new InstantCommand(() -> r.getO().specimenGrab180())
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.grab2()),
                        new SevenChamber(r)
                                .alongWith(
                                        new WaitCommand(250)
                                                .andThen(
                                                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.score2()).setCompletionThreshold(0.975)
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.grab3(), true, 1)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.score3(), true, 1).setCompletionThreshold(0.975)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.grab4(), true, 1)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.score4(), true, 1).setCompletionThreshold(0.975)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.grab5(), true, 1)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.score5(), true, 1).setCompletionThreshold(0.975)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.grab6(), true, 1)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.score6(), true, 1).setCompletionThreshold(0.975)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.grab7(), true, 1),
                        new InstantCommand(() -> { r.getO().close(); r.getO().specimenGrab180Yellow();})
                                .andThen(
                                        new WaitCommand(250),
                                        new FollowPath(r.getF(), config.core.paths.SixSpecOneSample.score7(), true, 1).setCompletionThreshold(0.975)
                                                .alongWith(
                                                        new InstantCommand(() -> r.getO().setRotateState(Outtake.RotateState.SCORE)),
                                                        new WaitCommand(250)
                                                                .andThen(
                                                                        new InstantCommand(() -> r.getI().cloud()),
                                                                        new InstantCommand(() -> r.getO().score()),
                                                                        new InstantCommand(() -> r.getL().toHighBucket())
                                                                )
                                                ),
                                        new InstantCommand(() -> r.getO().open())
                                                .andThen(
                                                        new WaitCommand(250),
                                                        new InstantCommand(() -> r.getL().pidOff())
                                                                .alongWith(
                                                                        new InstantCommand(() -> r.getI().hover()),
                                                                        new InstantCommand(() -> r.getO().transfer())
                                                                )
                                                )
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

            config.core.paths.SixSpecOneSample.sub2 = r.getM().getManualPoses().get(0).getPose().copy();
            config.core.paths.SixSpecOneSample.sub3 = r.getM().getManualPoses().get(1).getPose().copy();

            r.getT().addData("sub2", config.core.paths.SixSpecOneSample.sub2);
            r.getT().addData("sub3", config.core.paths.SixSpecOneSample.sub3);

            r.getT().addLine();

            config.core.paths.SixSpecOneSample.score1.setY(r.getM().getManualPoses().get(0).getPose().getY());
            config.core.paths.SixSpecOneSample.score2.setY(r.getM().getManualPoses().get(1).getPose().getY());

            r.getT().addData("score1", config.core.paths.SixSpecOneSample.score1);
            r.getT().addData("score2", config.core.paths.SixSpecOneSample.score2);
        }
        r.getT().update();*/
    }
}
