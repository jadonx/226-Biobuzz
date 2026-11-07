package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="MyFirstProgram")
public class MyFirstProgram extends OpMode {
    DcMotor motor1;

    @Override
    public void init() {
        motor1 = hardwareMap.get(DcMotor.class, "mymotor");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            // Do something
        }
    }
}
