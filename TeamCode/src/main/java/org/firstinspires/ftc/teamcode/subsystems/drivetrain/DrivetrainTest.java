package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.CommandScheduler;

@TeleOp(name="DriveTrainTest", group="Test")
public class DrivetrainTest extends OpMode {
    Drivetrain drivetrain;
    CommandScheduler scheduler;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap, gamepad1, telemetry);

        scheduler = new CommandScheduler();

        scheduler.registerSubsystem(drivetrain);
    }

    @Override
    public void loop() {
        scheduler.run();

        telemetry.update();
    }
}
