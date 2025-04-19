package opmode.auto;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.commands.FollowPath;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import config.commands.AlignSixSpecPush;
import config.commands.CSubmersible;
import config.commands.Chamber;
import config.commands.ForwardChamber;
import config.commands.SSubmersible;
import config.commands.SevenChamber;
import config.commands.Specimen;
import config.commands.Submersible;
import config.core.util.Alliance;
import config.core.util.OpModeCommand;
import config.core.Robot;

@Autonomous(name = "6+0 Push", group = "Unsigma")
public class SixSpecPush extends OpModeCommand {
    Robot r;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, telemetry, Alliance.BLUE, config.core.paths.SixSpecPush.start, true, 1);
        r.getI().init();
        r.getO().sevenInit();
        r.getO().close();
        r.getE().toZero();
        r.getJ().off();
        r.getT().addData("init", true);
        r.getT().addData("sub2", config.core.paths.SixSpecPush.sub2);
        r.getT().update();

        schedule(
                new RunCommand(r::aPeriodic),
                new SequentialCommandGroup(
                        new ForwardChamber(r)
                                .alongWith(
                                        new WaitCommand(500)
                                                .andThen(
                                                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.score1())
                                                ),
                                        new WaitCommand(500)
                                                .andThen(
                                                        new InstantCommand(
                                                                () -> {
                                                                    r.getI().hover();
                                                                    r.getE().toFull();
                                                                }
                                                        )
                                                )
                                ),
                        new AlignSixSpecPush(r, r.getM().getManualPoses().get(0)),
                        new SSubmersible(r),
                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.deposit2())
                                .alongWith(
                                        new WaitCommand(500)
                                                .andThen(
                                                        new InstantCommand(() -> r.getE().toFull())
                                                )
                                )
                                .andThen(new InstantCommand(() -> r.getI().open())),
                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.push())
                                .alongWith(
                                        new WaitCommand(250)
                                                .andThen(
                                                        new InstantCommand(() -> r.getI().open())
                                                                .andThen(
                                                                        new WaitCommand(250)
                                                                                .andThen(new InstantCommand(() -> r.getI().specimen()))
                                                                ),
                                                        new WaitCommand(500),
                                                        new InstantCommand(() -> r.getE().toZero()),
                                                        new WaitCommand(250),
                                                        new InstantCommand(() -> r.getO().specimenGrab180())
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.grab2()).setCompletionThreshold(0.99),
                        new SevenChamber(r)
                                .alongWith(
                                        new WaitCommand(250)
                                                .andThen(
                                                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.score2()).setCompletionThreshold(0.99)
                                                )
                                ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.grab3(), true, 1).setCompletionThreshold(0.99)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SixSpecPush.score3(), true, 1).setCompletionThreshold(0.99)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.grab4(), true, 1).setCompletionThreshold(0.99)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SixSpecPush.score4(), true, 1).setCompletionThreshold(0.99)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.grab5(), true, 1).setCompletionThreshold(0.99)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(150)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SixSpecPush.score5(), true, 1).setCompletionThreshold(0.99)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.grab6(), true, 1).setCompletionThreshold(0.99)
                        /*.alongWith(new Specimen(r))*/,
                        new SevenChamber(r).alongWith(
                                new WaitCommand(250)
                                        .andThen(
                                                new FollowPath(r.getF(), config.core.paths.SixSpecPush.score6(), true, 1).setCompletionThreshold(0.99)
                                        )
                        ),
                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.grab6(), true, 1).setCompletionThreshold(0.975)//,
//                        new FollowPath(r.getF(), config.core.paths.SixSpecPush.park(), true, 1)
//                                .alongWith(
//                                        new InstantCommand(
//                                                () -> {
//                                                    new WaitCommand(250);
//                                                    r.getO().transfer();
//                                                    r.getI().hover();
//                                                }
//                                        ),
//                                        new WaitCommand(500)
//                                                .andThen(
//                                                        new InstantCommand(() -> r.getE().toFull())
//                                                )
//                                )
                )
        );
    }

    @Override
    public void init_loop() {
        super.init_loop();
        r.getM().update(gamepad2);

       // config.core.paths.SixSpecPush.score1 = new Pose(config.core.paths.SixSpecPush.score1.getX(), r.getM().getManualPoses().get(0).getPose().getY());

        r.getT().addLine();
        r.getT().addData("sub2", config.core.paths.SixSpecPush.sub2);
        r.getT().addLine();
        r.getT().addData("score1", config.core.paths.SixSpecPush.score1);
        r.getT().addLine();
        r.getT().update();
    }
}
