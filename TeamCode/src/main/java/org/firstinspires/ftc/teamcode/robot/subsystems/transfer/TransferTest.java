package org.firstinspires.ftc.teamcode.robot.subsystems.transfer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TransferTest", group="Test")
public class TransferTest extends OpMode {
    Transfer transfer;

    @Override
    public void init() {
        transfer = new Transfer(hardwareMap);
    }

    @Override
    public void loop() {
        transfer.update(gamepad1.right_trigger);
    }
}