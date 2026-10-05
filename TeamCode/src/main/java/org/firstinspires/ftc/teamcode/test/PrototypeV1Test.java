package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.utils.CommandScheduler;

@TeleOp(name="PrototypeV1Test", group="Test")
public class PrototypeV1Test extends OpMode {
    // Subsystems
    Drivetrain drivetrain;
    Intake intake;

    CommandScheduler scheduler;

    @Override
    public void init() {
        // Subsystems
        drivetrain = new Drivetrain(hardwareMap);
        intake = new Intake(hardwareMap);

        scheduler = new CommandScheduler();
    }

    @Override
    public void loop() {
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;

        double intakePower = gamepad1.right_trigger;

        drivetrain.update(x, y, rx);
        intake.update(intakePower);

        scheduler.run(); // <- only takes care of commands

        telemetry.update();
    }
}
