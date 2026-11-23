package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.auto.roadrunner.Drawing;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.Drivetrain;

@Config
@TeleOp(name = "TeleOpAutoAimDrive", group = "Drive")
public class getOrientation extends LinearOpMode {
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
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();

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

        //Pinpoint methods




        waitForStart();

        while (opModeIsActive()) {
            double currentAngleDeg = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double robotHeadingRad = Math.toRadians(currentAngleDeg);

            double stickY = -gamepad1.left_stick_y;
            double stickX = gamepad1.left_stick_x;

            double x = stickX * Math.cos(-robotHeadingRad) - stickY * Math.sin(-robotHeadingRad);
            double y = stickX * Math.sin(-robotHeadingRad) + stickY * Math.cos(-robotHeadingRad);

            double error = targetAngle - currentAngleDeg;
            while (error > 180) error -= 360;
            while (error < -180) error += 360;

// Calculate how fast the error is changing (the speed of rotation)
            double derivative = error - lastError;
            lastError = error;

// Combine Proportional power with Derivative braking power
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

            TelemetryPacket telemetryPacket = new TelemetryPacket();
            telemetryPacket.put("Current Angle", currentAngleDeg);
            telemetryPacket.put("Target Angle", targetAngle);
            telemetryPacket.put("Heading Error", error);
            telemetryPacket.put("Turn Power", turnPower);
            telemetry.addData("kP Value", kP);
            telemetry.addData("kD Value", kD);
            telemetry.update();
            dashboard.sendTelemetryPacket(telemetryPacket);
        }
    }
}

