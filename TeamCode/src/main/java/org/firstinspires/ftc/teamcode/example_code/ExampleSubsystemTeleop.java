package org.firstinspires.ftc.teamcode.example_code;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.CommandScheduler;

@TeleOp(name="ExampleSubsystemTeleop", group="Test")
public class ExampleSubsystemTeleop extends OpMode {
    ExampleSubsystem exampleSubsystem;
    CommandScheduler scheduler;

    @Override
    public void init() {
        exampleSubsystem = new ExampleSubsystem(hardwareMap, gamepad1);

        scheduler = new CommandScheduler();

        scheduler.registerSubsystem(exampleSubsystem);
    }

    @Override
    public void loop() {
        scheduler.run();
    }
}
