package opmode.auto;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.commands.FollowPath;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import config.commands.Bucket;
import config.commands.Chamber;
import config.commands.ForwardChamber;
import config.commands.Specimen;
import config.commands.Submersible;
import config.commands.Transfer;
import config.core.util.Alliance;
import config.core.util.OpModeCommand;
import config.core.Robot;

@Autonomous(name = "0+4", group = "...Unsigma")
public class FourSamp extends OpModeCommand {
    Robot r;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, telemetry, Alliance.BLUE, config.core.paths.FourSamp.start, false, 0);
        r.getI().init();
        r.getO().sevenInit();
        r.getO().close();
        r.getE().toZero();
        r.getT().addData("init", true);
        r.getT().update();

        schedule(
                new RunCommand(r::aPeriodic),
                new SequentialCommandGroup(
                        new Bucket(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.FourSamp.score1())
                                ),
                        new FollowPath(r.getF(), config.core.paths.FourSamp.grab2())
                                .andThen(
                                        new InstantCommand(() -> r.getE().toFull())
                                                .andThen(
                                                        new WaitCommand(500),
                                                        new Submersible(r),
                                                        new Transfer(r)
                                                )
                                ),
                        new Bucket(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.FourSamp.score2())
                                ),
                        new FollowPath(r.getF(), config.core.paths.FourSamp.grab3())
                                .andThen(
                                        new InstantCommand(() -> r.getE().toFull())
                                                .andThen(
                                                        new WaitCommand(500),
                                                        new Submersible(r),
                                                        new Transfer(r)
                                                )
                                ),
                        new Bucket(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.FourSamp.score3())
                                ),
                        new FollowPath(r.getF(), config.core.paths.FourSamp.grab4())
                                .andThen(
                                        new InstantCommand(() -> r.getE().toFull())
                                                .andThen(
                                                        new WaitCommand(500),
                                                        new Submersible(r),
                                                        new Transfer(r)
                                                )
                                ),
                        new Bucket(r)
                                .alongWith(
                                        new FollowPath(r.getF(), config.core.paths.FourSamp.score4())
                                ),
                        new FollowPath(r.getF(), config.core.paths.FourSamp.park())
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
}