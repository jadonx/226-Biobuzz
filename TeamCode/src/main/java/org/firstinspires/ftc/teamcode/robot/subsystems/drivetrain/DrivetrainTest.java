package org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot.Constants;

@TeleOp(name="DrivetrainTest", group="Test")
public class DrivetrainTest extends OpMode {
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private IMU imu;

    FtcDashboard dashboard;
    TelemetryPacket packet;

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

        imu = hardwareMap.get(IMU.class, Constants.imu);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);

        dashboard = FtcDashboard.getInstance();

        packet = new TelemetryPacket();
    }

    public void driveFieldCentric(double x, double y, double rx) {

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        driveRobotCentric(rotX, rotY, rx);
    }

    public void driveRobotCentric(double x, double y, double rx) {
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    @Override
    public void loop() {
        driveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        packet.put("Left Stick X", gamepad1.left_stick_x);
        packet.put("Left Stick Y", -gamepad1.left_stick_y);
        packet.put("Right Stick X", gamepad1.right_stick_x);

        dashboard.sendTelemetryPacket(packet);
    }
}