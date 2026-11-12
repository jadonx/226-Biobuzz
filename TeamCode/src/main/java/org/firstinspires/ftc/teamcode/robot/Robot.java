package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.robot.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.transfer.Transfer;

public class Robot {
    private Drivetrain drivetrain;
    private Intake intake;
    private Transfer transfer;

    private Shooter shooter;

    private Gamepad gamepad1;

    public Robot(HardwareMap hardwareMap, Gamepad gamepad1) {
        drivetrain = new Drivetrain(hardwareMap);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        shooter = new Shooter(hardwareMap);

        this.gamepad1 = gamepad1;
    }

    public void update() {
        updateDrive();
        updateIntake();
        updateTransfer();
        updateShooter();
    }

    private void updateDrive() {
        drivetrain.driveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        if (gamepad1.x) {
            drivetrain.resetHeading();
        }
    }

    private void updateIntake() {
        intake.update(Math.max(gamepad1.right_trigger, gamepad1.left_trigger));
    }

    private void updateShooter() {
        if(gamepad1.a){
            shooter.setTargetRPM(4500);
        }
        if(gamepad1.b){
            shooter.setTargetRPM(0);
        }
        shooter.update();
    }

    private void updateTransfer() {
        transfer.update(gamepad1.left_trigger);
    }

    public double getFrontLeft() {
        return drivetrain.getFrontLeftPower();
    }

    public double getFrontRight() {
        return drivetrain.getFrontRight();
    }

    public double getBackLeft() {
        return drivetrain.getBackLeft();
    }

    public double getBackRight() {
        return drivetrain.getBackRight();
    }

    public double getHeadingDegrees() {
        return drivetrain.getHeadingDegrees();
    }

    public double getHeadingRadians() {
        return drivetrain.getHeadingRadians();
    }
}