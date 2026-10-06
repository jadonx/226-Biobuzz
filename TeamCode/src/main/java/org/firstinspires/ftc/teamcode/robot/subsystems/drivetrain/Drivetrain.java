package org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.utils.Subsystem;

public class Drivetrain implements Subsystem {
    // Motor and IMU variables (frontLeft, frontRight, etc.)
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private IMU imu;

    public Drivetrain(HardwareMap hardwareMap) {
        configureDriveMotors(hardwareMap);
        configureIMU(hardwareMap);
    }

    // Drive methods
    public void driveFieldCentric(double x, double y, double rx) {

        double botHeading = getHeadingRadians();

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        driveRobotCentric(rotX, rotY, rx);
    }

    public void driveFieldCentricWithAutoAim(double x, double y, double angle) {
        // TODO: Keep x and y field centric same, change rx for auto-rotate
    }

    private void driveRobotCentric(double x, double y, double rx) {
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

    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    // IMU
    public double getHeadingRadians() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public double getHeadingDegrees() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void resetHeading() {
        imu.resetYaw();
    }

    // Configuration Methods
    private void configureDriveMotors(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotorEx.class, Constants.driveMotorFL);
        frontRight = hardwareMap.get(DcMotorEx.class, Constants.driveMotorFR);
        backLeft = hardwareMap.get(DcMotorEx.class, Constants.driveMotorBL);
        backRight = hardwareMap.get(DcMotorEx.class, Constants.driveMotorBR);

        // TODO: Reverse Motors
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void configureIMU(HardwareMap hardwareMap) {
        imu = hardwareMap.get(IMU.class, Constants.imu);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
    }
}
