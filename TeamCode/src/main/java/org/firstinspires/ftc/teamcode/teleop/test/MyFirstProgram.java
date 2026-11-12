package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="MyFirstProgram")
public class MyFirstProgram extends OpMode {
    CRServo CRservo;

    @Override
    public void init() {
        CRservo = hardwareMap.get(CRServo.class, "flowerIntake");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            CRservo.setPower(0.25);
        }
        if (gamepad1.b) {
            CRservo.setPower(0);
        }
        if (gamepad1.x) {
            CRservo.setPower(-0.25);
        }
    }
}
