package org.firstinspires.ftc.teamcode.example_code;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Constants;
import org.firstinspires.ftc.teamcode.utils.Subsystem;

public class ExampleSubsystem implements Subsystem {
    private DcMotor motor1;

    private Gamepad gamepad1;

    public ExampleSubsystem(HardwareMap hardwareMap, Gamepad gamepad1) {
        motor1 = hardwareMap.get(DcMotor.class, Constants.shooterMotor1);
        this.gamepad1 = gamepad1;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        if (gamepad1.a) {
            motor1.setPower(0.5);
        }
        else {
            motor1.setPower(0);
        }
    }
}
