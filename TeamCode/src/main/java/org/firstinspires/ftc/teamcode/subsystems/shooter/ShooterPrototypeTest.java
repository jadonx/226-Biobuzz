package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.utils.Constants;

@TeleOp(name="ShooterPrototypeTest")
public class ShooterPrototypeTest extends OpMode {
    DcMotorEx shooter1;
    double shooterSpeed;

    @Override
    public void init() {
        shooter1 = hardwareMap.get(DcMotorEx.class, Constants.shooterMotor1);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterSpeed = 0;
    }

    @Override
    public void loop() {
        if (gamepad1.yWasPressed()) {
            shooterSpeed += 10;
        }

        if (gamepad1.aWasPressed()) {
            shooterSpeed -= 10;
        }

        shooterSpeed = Math.max(0, shooterSpeed);

        shooter1.setVelocity(shooterSpeed);

        telemetry.addData("Shooter speed ", shooterSpeed);
        telemetry.update();
    }
}
