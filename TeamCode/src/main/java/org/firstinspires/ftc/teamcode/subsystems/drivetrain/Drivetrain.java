package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorGoBildaPinpoint;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.utils.Constants;
import org.firstinspires.ftc.teamcode.utils.Subsystem;

public class Drivetrain implements Subsystem {
    // Motor and IMU variables (frontLeft, frontRight, etc.)
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private IMU imu;
    private GoBildaPinpointDriver pinpoint;

    // Gamepad variable (we need a gamepad to use as input)
    private Gamepad gamepad1;

    private Telemetry telemetry;

    public Drivetrain(HardwareMap hardwareMap, Gamepad gamepad1, Telemetry telemetry) {
        configureDriveMotors(hardwareMap);

        configureIMU(hardwareMap);

        configurePinpoint(hardwareMap);

        this.gamepad1 = gamepad1;

        this.telemetry = telemetry;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        if (gamepad1.xWasPressed()) { imu.resetYaw(); }

        driveUpdate(x, y, rx, botHeading);

        pinpointUpdate();
    }

    private void driveUpdate(double x, double y, double rx, double botHeading) {
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    private void pinpointUpdate() {
        pinpoint.update();
        Pose2D pose2D = pinpoint.getPosition();

        if(gamepad1.a){
            pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
        }

        updateTelemetry(pose2D);
    }

    private void updateTelemetry(Pose2D pose2D) {
        telemetry.addLine("Push your robot around to see it track");
        telemetry.addLine("Press A to reset the position");

        telemetry.addData("X coordinate (IN)", pose2D.getX(DistanceUnit.INCH));
        telemetry.addData("Y coordinate (IN)", pose2D.getY(DistanceUnit.INCH));
        telemetry.addData("Heading angle (DEGREES)", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
    }

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

    private void configurePinpoint(HardwareMap hardwareMap) {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.pinpoint);

        pinpoint.setOffsets(54.5, -131.4, DistanceUnit.MM);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        pinpoint.resetPosAndIMU();
    }
}
