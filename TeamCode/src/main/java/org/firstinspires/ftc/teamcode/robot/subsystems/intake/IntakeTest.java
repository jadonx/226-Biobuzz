package org.firstinspires.ftc.teamcode.robot.subsystems.intake;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="IntakeTest", group="Test")
public class IntakeTest extends OpMode {
    Intake intake;

    @Override
    public void init() {
        intake = new Intake(hardwareMap);
    }

    @Override
    public void loop() {
        intake.update(gamepad1.right_trigger);
    }
}
