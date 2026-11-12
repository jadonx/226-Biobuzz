package org.firstinspires.ftc.teamcode.teleop.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot.Constants;

@Config
@TeleOp(name="BuildTest", group="Test")
public class BuildTest extends OpMode {
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private DcMotorEx intake;
    private DcMotorEx transfer;
    private DcMotorEx shooter;
    private DcMotorEx turret;

    public static double driveMotorPower;
    public static double intakeMotorPower;
    public static double transferMotorPower;
    public static double shooterMotorPower;
    public static double turretMotorPower;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotorEx.class, Constants.driveMotorFL);
        frontRight = hardwareMap.get(DcMotorEx.class, Constants.driveMotorFR);
        backLeft = hardwareMap.get(DcMotorEx.class, Constants.driveMotorBL);
        backRight = hardwareMap.get(DcMotorEx.class, Constants.driveMotorBR);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hardwareMap.get(DcMotorEx.class, Constants.intakeMotor);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        transfer = hardwareMap.get(DcMotorEx.class, Constants.transferMotor);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter = hardwareMap.get(DcMotorEx.class, Constants.shooterMotor);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        turret = hardwareMap.get(DcMotorEx.class, Constants.turretMotor);
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        turret.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void loop() {
        frontLeft.setPower(driveMotorPower);
        frontRight.setPower(driveMotorPower);
        backLeft.setPower(driveMotorPower);
        backRight.setPower(driveMotorPower);
        intake.setPower(intakeMotorPower);
        transfer.setPower(transferMotorPower);
        shooter.setPower(shooterMotorPower);
        turret.setPower(turretMotorPower);
    }
}