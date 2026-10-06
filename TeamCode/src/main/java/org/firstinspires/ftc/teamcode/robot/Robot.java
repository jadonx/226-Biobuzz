package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.robot.Constants.AllianceColor;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.Drivetrain;

public class Robot {
    private Drivetrain drivetrain;

    private Gamepad gamepad1;
    private Gamepad gamepad2;

    // The goal we want to aim at (the one in the same half of the field as we are)
    private Pose2D currentGoalTarget;

    private boolean usingAutoAim;

    public Robot(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2, AllianceColor allianceColor) {
        drivetrain = new Drivetrain(hardwareMap);

        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        usingAutoAim = false;
    }

    public void update() {
        // logic for updating the drive for regular/auto-aim
        updateDrive();
    }

    public void stop() {

    }

    // DRIVETRAIN
    private void updateDrive() {
        if (gamepad1.aWasPressed()) {
            usingAutoAim = true;
        }
        else if (gamepad1.yWasPressed()) {
            usingAutoAim = false;
        }

        if (usingAutoAim) {
            // TODO: Create update method for drive with auto-aim
        }
        else {
            drivetrain.driveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }
    }

    // PINPOINT
    private void updateGoalTarget(Pose2D currentPosition) {
        // TODO: Logic for updating target goal when we are in either sides of the field
    }

    private void calculateAutoAimRotadtion(double currentRotation) {
        // TODO: Given our current rotation + the pinpoint's x/y position, find the angle towards the goal
    }

    private void calculateGoalDistance() {
        // TODO: Using the currentGoalTarget, find our distance from the goal (used for shooter calculation)
    }

    // SHOOTER
    private void updateShooter() {

    }

    // INTAKE
    private void updateIntake() {

    }
}
