package org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
@TeleOp(name = "Drivetrain AutoAim Test", group = "Test")
public class AutoAimTestTeleOp extends LinearOpMode {

    public static double kP = 0.003;
    //public static double deadzone = 5.0;

    private DcMotor leftFront, leftBack, rightFront, rightBack;
    private IMU imu;
    private double lastError = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();

        // 1. Initialize IMU
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);

        // 2. Initialize Motors
        leftFront = hardwareMap.get(DcMotorEx.class,"frontLeft");
        leftBack = hardwareMap.get(DcMotor.class, "backLeft");
        rightFront = hardwareMap.get(DcMotor.class, "frontRight");
        rightBack = hardwareMap.get(DcMotor.class, "backRight");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            // Reset heading if start is pressed
            if (gamepad1.start) {
                imu.resetYaw();
            }

            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double targetAngle = 0.0;

            // get robot heading in radians for field-centric math
            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // rotate joystick inputs based on current robot heading
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            // Get current heading in degrees for error calculation
            double currentAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double error = targetAngle - currentAngle;

            while (error > 180) error -= 360;
            while (error < -180) error += 360;
            double derivative = error - lastError;
            lastError = error;

            double rx = error*kP;
            // Deadzone check
            //double rx = 0.0;
            //if (Math.abs(error) > deadzone) {
            //    rx = error * kP;
            //}
            //else{
            //    rx=0;

            //}
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            leftFront.setPower(frontLeftPower);
            leftBack.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightBack.setPower(backRightPower);

            // Send Dashboard & Driver Station Telemetry
            TelemetryPacket telemetryPacket = new TelemetryPacket();
            telemetryPacket.put("Current Heading", currentAngle);
            telemetryPacket.put("Heading Error", error);
            telemetryPacket.put("Rotation Power (rx)", rx);
            telemetryPacket.put("kP Value", kP);
            //telemetryPacket.put("DeadZone Degrees", deadzone);
            dashboard.sendTelemetryPacket(telemetryPacket);

            telemetry.addData("Current Heading", currentAngle);
            telemetry.addData("Heading Error", error);
            telemetry.addData("Rotation Power (rx)", rx);
            telemetry.addData("kP Value", kP);
            //telemetry.addData("DeadZone Degrees", deadzone);
            telemetry.update();
        }

        // Stop motors when OpMode ends
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
}

/*
@TeleOp(name = "Drivetrain AutoAim Test", group = "Test")
public class AutoAimTestTeleOp extends LinearOpMode {

    private Drivetrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        drivetrain = new Drivetrain(hardwareMap);
        String mode = "";
        boolean autoAimOn= false;
        waitForStart();

        while (opModeIsActive()) {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rx = gamepad1.right_stick_x;

            if (gamepad1.options || gamepad1.start) {
                drivetrain.resetHeading();
            }

           double targetAngleDegrees = 0.0;

            if (gamepad1.a) {
                autoAimOn = true;
            } else if (gamepad1.b || Math.abs(rx) > 0.2) {
                // Automatically turn off auto-aim if B is pressed or if driver moves right stick
                autoAimOn = false;
            }

// Execute movement based on state
            if (autoAimOn) {
                drivetrain.driveFieldCentricWithAutoAim(x, y, targetAngleDegrees);
                mode = "Auto Aim On";
            } else {
                drivetrain.driveFieldCentric(x, y, rx);
                mode = "Auto Aim Off";
            }

                TelemetryPacket telemetryPacket = new TelemetryPacket();
                telemetryPacket.put("Current Heading ", drivetrain.getHeadingDegrees());
                telemetryPacket.put("Target Angle ", targetAngleDegrees);
                telemetryPacket.put("Mode ", mode);
                telemetryPacket.put("kP Value", drivetrain.getKP());
                dashboard.sendTelemetryPacket(telemetryPacket);

                // Telemetry Output for testing and tuning
                telemetry.addData("Current Heading ", drivetrain.getHeadingDegrees());
                telemetry.addData("Target Angle ", targetAngleDegrees);
                telemetry.addData("Mode ", mode);
                telemetry.addData("kP Value", drivetrain.getKP());
                telemetry.update();
        }

        drivetrain.stop();
    }
}
}
*/

