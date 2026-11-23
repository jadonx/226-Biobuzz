package org.firstinspires.ftc.teamcode.robot.subsystems.pinpoint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.auto.roadrunner.Drawing;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.Drivetrain;

import org.firstinspires.ftc.teamcode.robot.subsystems.shooter.Turret;

@Config
@TeleOp(name="LocalizationBlue")
public class LocalizationBlue extends OpMode {

    public static double targetAngle = 0.0;
    public static double kP = 0.004;
    public static double kD = 0.0001;
    public static double xOffset = Constants.pinpointXOffset;
    public static double yOffset = Constants.pinpointYOffset;

    private Pinpoint pinpoint;
    private Drivetrain drivetrain;
    private Turret turret;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap);
        pinpoint = new Pinpoint(hardwareMap);
        pinpoint.setOffsets(xOffset, yOffset);
        turret = new Turret(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.x) {
            drivetrain.resetHeading();
        }

        drivetrain.driveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        pinpoint.update();

        Pose2D targetGoal = pinpoint.getClosestBlueGoal();
        String targetGoalName = (targetGoal == Constants.blueGoal1) ? "Blue Goal 1" : "Blue Goal 2";

        double currentX = pinpoint.getX();
        double currentY = pinpoint.getY();
        double currentAngle = pinpoint.getHeading();
        double distanceToGoal = pinpoint.getDistanceTo(targetGoal);
        double targetAngleRadians = pinpoint.getAngleToRadians(targetGoal);
        double targetAngleDegrees = pinpoint.getAngleToDegrees(targetGoal);
        double angleError = pinpoint.getAngleError(targetGoal);

        turret.setTargetAngle(angleError);
        turret.update();

        telemetry.addData("Closest Goal", targetGoalName);
        telemetry.addData("X (in)", String.format("%.2f", currentX));
        telemetry.addData("Y (in)", String.format("%.2f", currentY));
        telemetry.addData("Heading (deg)", String.format("%.2f", currentAngle));
        telemetry.addData("Distance to Goal (in)", String.format("%.2f", distanceToGoal));
        telemetry.addData("Target Angle (deg)", String.format("%.2f", targetAngleDegrees));
        telemetry.addData("Angle Error (deg)", String.format("%.2f", angleError));
        telemetry.addData("Turret Target (deg)", String.format("%.2f", turret.getTargetAngle()));
        telemetry.addData("Turret Current (deg)", String.format("%.2f", turret.getCurrentAngle()));

        telemetry.update();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket telemetryPacket = new TelemetryPacket();
        telemetryPacket.put("Closest Goal", targetGoalName);
        telemetryPacket.put("X (in)", currentX);
        telemetryPacket.put("Y (in)", currentY);
        telemetryPacket.put("Distance to Goal (in)", distanceToGoal);
        telemetryPacket.put("Target Angle (deg)", targetAngleDegrees);
        telemetryPacket.put("Angle Error (deg)", angleError);
        telemetryPacket.put("Turret Target (deg)", turret.getTargetAngle());
        telemetryPacket.put("Turret Current (deg)", turret.getCurrentAngle());


        telemetryPacket.fieldOverlay().setStroke("#4CAF50");
        telemetryPacket.fieldOverlay().strokeLine(
                currentX,
                currentY,
                targetGoal.getX(DistanceUnit.INCH),
                targetGoal.getY(DistanceUnit.INCH)
        );

        telemetryPacket.fieldOverlay().setStroke("#3F51B5");
        Drawing.drawRobot(
                telemetryPacket.fieldOverlay(),
                new Pose2d(currentX, currentY, targetAngleRadians)
        );
        dashboard.sendTelemetryPacket(telemetryPacket);
    }
}