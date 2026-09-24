package org.firstinspires.ftc.teamcode.example_code;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Subsystem;

@Disabled
public class Arm implements Subsystem {
    private DcMotor motor;

    private double targetPosition;

    public Arm(HardwareMap hardwareMap, Gamepad gamepad1) {
        motor = hardwareMap.get(DcMotor.class, "arm");

        targetPosition = 0;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        double error = targetPosition - motor.getCurrentPosition();

        motor.setPower(error * 0.001);
    }

    public void setTargetPosition(double position) {
        targetPosition = position;
    }

    public boolean atTarget() {
        return (targetPosition - motor.getCurrentPosition()) < 10;
    }
}
