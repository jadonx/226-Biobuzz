package org.firstinspires.ftc.teamcode.example_code;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.CommandScheduler;

@Disabled
@TeleOp
public class ArmTeleop extends OpMode {
    Arm arm;
    CommandScheduler scheduler;

    @Override
    public void init() {
        // Create our subsystem
        arm = new Arm(hardwareMap, gamepad1);

        // Create scheduler
        scheduler = new CommandScheduler();

        // Tell scheduler that the Arm exists.
        // This causes arm.update() to run every loop.
        scheduler.registerSubsystem(arm);
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            scheduler.schedule(new MoveArmCommand(arm, 1000));
        }

        if (gamepad1.bWasPressed()) {
            scheduler.schedule(new MoveArmCommand(arm, 0));
        }

        scheduler.run();
    }

    @Override
    public void stop() {
        scheduler.cancelAll();
    }
}
