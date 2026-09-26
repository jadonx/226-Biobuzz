package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.utils.CommandScheduler;

@TeleOp(name="PrototypeV1Test", group="Test")
public class PrototypeV1Test extends OpMode {
    Drivetrain drivetrain;
    Intake intake;

    CommandScheduler commandScheduler;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap, gamepad1, telemetry);
        intake = new Intake(hardwareMap, gamepad1);

        commandScheduler = new CommandScheduler();

        commandScheduler.registerSubsystem(drivetrain);
        commandScheduler.registerSubsystem(intake);
    }

    @Override
    public void loop() {
        commandScheduler.run();

        telemetry.update();
    }
}
