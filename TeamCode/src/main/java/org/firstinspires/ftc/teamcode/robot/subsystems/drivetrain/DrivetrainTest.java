package org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="DrivetrainTest", group="Test")
public class DrivetrainTest extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        telemetry.addData("gamepad y", -gamepad1.left_stick_y);
        telemetry.addData("gamepad x", gamepad1.left_stick_x);

        telemetry.update();
    }
}
