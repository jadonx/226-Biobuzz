package org.firstinspires.ftc.teamcode.robot.subsystems.pinpoint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.auto.roadrunner.Drawing;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.Drivetrain;

@Config
@TeleOp(name="Localization")
public class Localization extends OpMode {

    public static double targetAngle = 0.0;
    public static double kP = 0.004;

    public static double kD = 0.0001; // Tune this to act as your brake
    private double lastError = 0.0;
    private IMU imu;
    private DcMotorEx leftFront, leftBack, rightFront, rightBack;
    private GoBildaPinpointDriver pinpoint;
    public static double xOffset = Constants.pinpointXOffset;
    public static double yOffset = Constants.pinpointYOffset;
    private Drivetrain drivetrain;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap);
        FtcDashboard dashboard = FtcDashboard.getInstance();

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.pinpoint);

        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);

        pinpoint.setOffsets(xOffset, yOffset, DistanceUnit.MM);

        pinpoint.resetPosAndIMU();
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
    }

    @Override
    public void loop() {
        if (gamepad1.x) {
            drivetrain.resetHeading();
        }

        drivetrain.driveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        pinpoint.update();

        Pose2D pose2D = pinpoint.getPosition();
        double currentX = pose2D.getX(DistanceUnit.INCH);
        double currentY = pose2D.getY(DistanceUnit.INCH);
        double currentAngle = pose2D.getHeading(AngleUnit.DEGREES);

        double distanceToOrigin = Math.hypot(currentX, currentY);

//        if (currentY > 0)
//        {
//            targetGoal = Constants.blueGoal1;
//        }
//        else
//        {
//            targetGoal = Constants.blueGoal2;
//        }
//
//        double targetAngleRadians = Math.atan2(targetGoal.getY(DistanceUnit.INCH) - currentY, targetGoal.getX(DistanceUnit.INCH) - currentX);
//        double targetAngleDegrees = Math.toDegrees(targetAngleRadians);
//
//        double angleError = targetAngleDegrees - currentAngle;
//
//        while (angleError > 180)
//            angleError -= 360;
//        while (angleError <= -180)
//            angleError += 360;

        telemetry.addData("X (in)", String.format("%.2f", currentX));
        telemetry.addData("Y (in)", String.format("%.2f", currentY));
        telemetry.addData("Heading (deg)", String.format("%.2f", currentAngle));
        telemetry.addData("Distance to Origin (in)", String.format("%.2f", distanceToOrigin));
        telemetry.addData("kP Value", kP);
        telemetry.addData("kD Value", kD);
        telemetry.update();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket telemetryPacket = new TelemetryPacket();
        telemetryPacket.put("X (in)", currentX);
        telemetryPacket.put("Y (in)", currentY);
        telemetryPacket.put("Target Angle", targetAngle);
        telemetryPacket.put("X Offset", xOffset);
        telemetryPacket.put("Y Offset", yOffset);
        telemetryPacket.put("Distance to Origin", distanceToOrigin);

        telemetryPacket.fieldOverlay().setStroke("#3F51B5");
        Drawing.drawRobot(
                telemetryPacket.fieldOverlay(),
                new Pose2d(currentX, currentY, pose2D.getHeading(AngleUnit.RADIANS))
        );
        dashboard.sendTelemetryPacket(telemetryPacket);


        telemetry.update();
    }
}