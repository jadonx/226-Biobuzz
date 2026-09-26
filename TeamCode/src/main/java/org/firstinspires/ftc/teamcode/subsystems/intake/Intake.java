package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Constants;
import org.firstinspires.ftc.teamcode.utils.Subsystem;

public class Intake implements Subsystem {
    private DcMotorEx intake;

    private Gamepad gamepad1;

    public Intake(HardwareMap hardwareMap, Gamepad gamepad1) {
        intake = hardwareMap.get(DcMotorEx.class, Constants.intakeMotor);

        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.gamepad1 = gamepad1;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        intake.setPower(gamepad1.right_trigger);
    }
}
