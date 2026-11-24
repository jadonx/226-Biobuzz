package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.robot.subsystems.flowerintake.FlowerIntake;
import org.firstinspires.ftc.teamcode.robot.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.pinpoint.Pinpoint;
import org.firstinspires.ftc.teamcode.robot.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.robot.subsystems.turret.Turret;

public class Robot {
    private Drivetrain drivetrain;
    private Intake intake;
    private Transfer transfer;
    private Turret turret;
    private Shooter shooter;
    private FlowerIntake flowerIntake;
    private Pinpoint pinpoint;

    private LynxModule expansionHub;

    private Gamepad gamepad1;

    private boolean runIntake = false;
    private boolean runShooter = false;

    private boolean flowerIntakeIsUp = true;
    private boolean flowerIntakeMoving = false;

    public Robot(HardwareMap hardwareMap, Gamepad gamepad1) {
        drivetrain = new Drivetrain(hardwareMap);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        turret = new Turret(hardwareMap);
        shooter = new Shooter(hardwareMap);
        flowerIntake = new FlowerIntake(hardwareMap);
        pinpoint = new Pinpoint(hardwareMap);

        expansionHub = hardwareMap.get(LynxModule.class, Constants.expansionHub);

        this.gamepad1 = gamepad1;
    }

    public void update() {
        updateDrive();
        updateIntake();
        updateTransfer();
        updateTurret();
        updateShooter();
        updateFlowerIntake();
        updatePinpoint();
    }

    private void updateDrive() {
        drivetrain.driveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        if (gamepad1.x) {
            drivetrain.resetHeading();
        }
    }

    private void updateIntake() {
        if (gamepad1.bWasPressed()) {
            runIntake = !runIntake;
        } else if (gamepad1.left_trigger > 0.05) {
            intake.update(-gamepad1.left_trigger);
        } else if (runIntake) {
            intake.update(1.0);
        } else {
            intake.update(0.0);
        }
    }

    private void updateTransfer() {
        if (gamepad1.left_trigger > 0.05) {
            transfer.update(-gamepad1.left_trigger);
        } else {
            transfer.update(gamepad1.right_trigger);
        }
    }

    private void updateTurret() {
        double angleError = pinpoint.getAngleError(pinpoint.getClosestBlueGoal());
        turret.setTargetAngle(angleError);
        turret.update();
    }

    private void updateShooter() {
        if (gamepad1.yWasPressed()) {
            runShooter = !runShooter;
        } else if (runShooter) {
            if (shooter.getTargetRPM() < 3600) {
                shooter.setTargetRPM(shooter.getTargetRPM() + 200);
            } else {
                shooter.setTargetRPM(3700);
            }
        } else {
            shooter.setTargetRPM(0);
        }
        shooter.update();
    }

    private void updateFlowerIntake() {
        double current = expansionHub.getCurrent(CurrentUnit.AMPS)
                - drivetrain.getCurrent()
                - intake.getCurrent()
                - transfer.getCurrent()
                - shooter.getCurrent();

        if (gamepad1.aWasPressed() && !flowerIntakeMoving) {
            flowerIntakeMoving = true;
        }

        if (flowerIntakeMoving) {
            double power = flowerIntakeIsUp ? -1.0 : 1.0;

            boolean stalled = flowerIntake.update(power, current);

            if (stalled) {
                flowerIntakeMoving = false;
                flowerIntakeIsUp = !flowerIntakeIsUp;
            }
        }
    }

    private void updatePinpoint() {
        pinpoint.update();
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

    public double getCurrentShooter
}