package org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain;

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
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);

        pinpoint.resetPosAndIMU();
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);

        leftFront = hardwareMap.get(DcMotorEx.class, "frontLeft");
        leftBack = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightFront = hardwareMap.get(DcMotorEx.class, "frontRight");
        rightBack = hardwareMap.get(DcMotorEx.class, "backRight");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        drivetrain.driveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        Pose2D targetGoal;

        double currentAngleDeg = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        double robotHeadingRad = Math.toRadians(currentAngleDeg);

        double stickY = -gamepad1.left_stick_y;
        double stickX = gamepad1.left_stick_x;

        double x = stickX * Math.cos(-robotHeadingRad) - stickY * Math.sin(-robotHeadingRad);
        double y = stickX * Math.sin(-robotHeadingRad) + stickY * Math.cos(-robotHeadingRad);

        double error = targetAngle - currentAngleDeg;
        while (error > 180) error -= 360;
        while (error < -180) error += 360;

        double derivative = error - lastError;
        lastError = error;

        double turnPower = (error * kP) + (derivative * kD);

        double frontLeftPower = y + x + turnPower;
        double backLeftPower = y - x + turnPower;
        double frontRightPower = y - x - turnPower;
        double backRightPower = y + x - turnPower;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turnPower), 1.0);
        leftFront.setPower(frontLeftPower/denominator);
        leftBack.setPower(backLeftPower/denominator);
        rightFront.setPower(frontRightPower/denominator);
        rightBack.setPower(backRightPower/denominator);

        if (gamepad1.x) {
            drivetrain.resetHeading();
        }

        FtcDashboard dashboard = FtcDashboard.getInstance();
        pinpoint.update();

        pinpoint.setOffsets(xOffset, yOffset, DistanceUnit.MM);

        Pose2D pose2D = pinpoint.getPosition();
        double currentX = pose2D.getX(DistanceUnit.INCH);
        double currentY = pose2D.getY(DistanceUnit.INCH);
        double currentAngle = pose2D.getHeading(AngleUnit.DEGREES);

        double distanceToOrigin = Math.hypot(currentX, currentY);

        if (currentY > 0)
        {
            targetGoal = Constants.blueGoal1;
        }
        else
        {
            targetGoal = Constants.blueGoal2;
        }

        double targetAngleRadians = Math.atan2(targetGoal.getY(DistanceUnit.INCH) - currentY, targetGoal.getX(DistanceUnit.INCH) - currentX);
        double targetAngleDegrees = Math.toDegrees(targetAngleRadians);

        double angleError = targetAngleDegrees - currentAngle;

        while (angleError > 180)
            angleError -= 360;
        while (angleError <= -180)
            angleError += 360;


        telemetry.addData("x ", -pose2D.getX(DistanceUnit.INCH));
        telemetry.addData("y ", pose2D.getY(DistanceUnit.INCH));
        telemetry.addData("Heading (DEG)", currentAngle);
        telemetry.addData("Distance to Origin (IN)", distanceToOrigin);
        telemetry.addData("Degrees needed to Turn", angleError);

        TelemetryPacket telemetryPacket = new TelemetryPacket();
        telemetryPacket.put("Current Angle", currentAngleDeg);
        telemetryPacket.put("Target Angle", targetAngle);
        telemetryPacket.put("Heading Error", error);
        telemetryPacket.put("Turn Power", turnPower);
        telemetry.addData("kP Value", kP);
        telemetry.addData("kD Value", kD);
        telemetryPacket.fieldOverlay().setStroke("#3F51B5");
        Drawing.drawRobot(telemetryPacket.fieldOverlay(), new Pose2d(pose2D.getX(DistanceUnit.INCH), pose2D.getY(DistanceUnit.INCH), pose2D.getHeading(AngleUnit.RADIANS)));
        FtcDashboard.getInstance().sendTelemetryPacket(telemetryPacket);

        telemetryPacket.put("X Offset ", xOffset);
        telemetryPacket.put("Y Offset ", yOffset);
        telemetryPacket.put("Distance to Origin ", distanceToOrigin);
        telemetryPacket.put("Degrees to Turn ", angleError);
        dashboard.sendTelemetryPacket(telemetryPacket);

        telemetry.update();
    }
}