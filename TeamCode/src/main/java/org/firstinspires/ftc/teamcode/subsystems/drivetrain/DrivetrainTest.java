package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.CommandScheduler;

@TeleOp(name="DriveTrainTest", group="Test")
public class DrivetrainTest extends OpMode {
    // Subsystems
    Drivetrain drivetrain;

    // Commands
    ManualDriveCommand manualDriveCommand;

    CommandScheduler scheduler;

    @Override
    public void init() {
        // Subsystems
        drivetrain = new Drivetrain(hardwareMap);

        // Commands
        manualDriveCommand = new ManualDriveCommand(drivetrain, gamepad1);

        scheduler = new CommandScheduler();

        scheduler.registerSubsystem(drivetrain);

        scheduler.schedule(manualDriveCommand);
    }

    @Override
    public void loop() {
        scheduler.run();

        telemetry.update();
    }
}
