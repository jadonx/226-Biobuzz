package org.firstinspires.ftc.teamcode.example_code;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Subsystem;

@Disabled
public class Arm implements Subsystem {
    private DcMotor arm_motor;

    private double targetPosition;

    @Override
    public void init(HardwareMap hardwareMap) {
        arm_motor = hardwareMap.get(DcMotorEx.class, "arm_motor");

        targetPosition = 0;
    }

    @Override
    public void update() {
        double error = targetPosition - arm_motor.getCurrentPosition();

        arm_motor.setPower(error * 0.001);
    }

    public void setTargetPosition(double position) {
        targetPosition = position;
    }

    public boolean atTarget() {
        return (targetPosition - arm_motor.getCurrentPosition()) < 10;
    }
}
