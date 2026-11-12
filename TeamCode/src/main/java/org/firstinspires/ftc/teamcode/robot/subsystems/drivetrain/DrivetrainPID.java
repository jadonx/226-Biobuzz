package org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.robot.Constants;

import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Drivetrain PID")
@Config
//@Disabled

public class DrivetrainPID extends OpMode {

    // Motors
    public DcMotor frontLeft, frontRight, backLeft, backRight;

    // IMU
    public IMU imu;
    public YawPitchRollAngles robotOrientation;
    public double robotYaw;

    // Gamepad
    public double leftStickY, leftStickX, rightStickX;

    // PID
    public static double Kp = 0.5;
    public static double Ki = 0;
    public static double Kd = 0.1;

    public double targetYaw;
    double integralSum = 0;
    double lastError = 0;

    ElapsedTime timer = new ElapsedTime();

    FtcDashboard dashboard;
    TelemetryPacket packet;

    // FTC Dashboard

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, Constants.driveMotorFL);
        frontRight = hardwareMap.get(DcMotor.class, Constants.driveMotorFR);
        backLeft = hardwareMap.get(DcMotor.class, Constants.driveMotorBL);
        backRight = hardwareMap.get(DcMotor.class, Constants.driveMotorBR);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        dashboard = FtcDashboard.getInstance();
        packet = new TelemetryPacket();

        imu = hardwareMap.get(IMU.class, Constants.imu);
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);

        targetYaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    @Override
    public void loop() {
        leftStickY = -gamepad1.left_stick_y;
        leftStickX = gamepad1.left_stick_x;
        rightStickX = gamepad1.right_stick_x;

        robotOrientation = imu.getRobotYawPitchRollAngles();

        robotYaw = robotOrientation.getYaw(AngleUnit.RADIANS);
        robotYaw = (robotYaw + Math.PI) % (2 * Math.PI) - Math.PI;

        double rotX = leftStickX * Math.cos(-robotYaw) - leftStickY * Math.sin(-robotYaw);
        double rotY = leftStickX * Math.sin(-robotYaw) + leftStickY * Math.cos(-robotYaw);
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rightStickX), 1);

        //  || Math.abs(imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate) > 1
        if (Math.abs(rightStickX) > 0.1 || Math.abs(imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate) > 1) {
            targetYaw = robotYaw;
            lastError = 0;
        }

        if (gamepad1.x) {
            imu.resetYaw();
            targetYaw = 0;
            integralSum = 0;
            lastError = 0;
        }

        // PID Calculations
        double error = targetYaw - robotYaw;
        error = (error + Math.PI) % (2 * Math.PI) - Math.PI;

        if (Math.abs(error) < Math.toRadians(2)) { // 2° tolerance
            error = 0;
        }

        // Compute PID Terms
        double derivative = (error - lastError) / timer.seconds();
        integralSum += error * timer.seconds();
        double correction = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

        double rotationPower = Math.abs(rightStickX) > 0.1 ? rightStickX : -correction;

        frontLeft.setPower((rotY + rotX + rotationPower) / denominator);
        frontRight.setPower((rotY - rotX - rotationPower) / denominator);
        backLeft.setPower((rotY - rotX + rotationPower) / denominator);
        backRight.setPower((rotY + rotX - rotationPower) / denominator);

        lastError = error;
        timer.reset();

        // FTC Dashboard
        packet.put("Target: ", Math.toDegrees(targetYaw));
        packet.put("Actual: ", Math.toDegrees(robotYaw));
        packet.put("Error: ", Math.toDegrees(error));
        packet.put("Yaw Acceleration", Math.abs(imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate));

        dashboard.sendTelemetryPacket(packet);
    }
}