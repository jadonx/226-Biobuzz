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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@Config
@TeleOp(name = "getOrientation")
public class getOrientation extends LinearOpMode {
    public static double targetAngle = 0.0;
    public static double kP = 0.004;
    private IMU imu;
    private DcMotor leftFront, leftBack, rightFront, rightBack;

    //a)make a static vairvle for either target angle or current angle(dont remember which makes sense)
    //b)given error = current angle - target angle, set power of all the left chassis wheels to certain power and all right chassis wheels to rotate in the - direction
    //1b)divide the power value by a test value, this test value will be edited through testing to ensure that the speed of rotation isn't too fast-->leading to overrunning rotation--> infinite back and forth tiny rotations
    // 2b) make a method for driving certain power in left wheels and right wheels
    // c)send current angle and target angle into ftc dashboard. let the test value denominator be edittable through ftc dashboard

    @Override

    public void runOpMode() throws InterruptedException{
        FtcDashboard dashboard = FtcDashboard.getInstance();
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);

        leftFront = hardwareMap.get(DcMotorEx.class,"frontLeft");
        leftBack = hardwareMap.get(DcMotor.class, "backLeft");
        rightFront = hardwareMap.get(DcMotor.class, "frontRight");
        rightBack = hardwareMap.get(DcMotor.class, "backRight");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            double currentAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double error = targetAngle - currentAngle;
            while (error > 180) error -= 360;
            while (error < -180) error += 360;
            double turnPower = error * kP;

            setDrivePower(-turnPower, turnPower);

            TelemetryPacket telemetryPacket = new TelemetryPacket();
            telemetryPacket.put("Current Angle", imu.getRobotYawPitchRollAngles().getYaw());
            telemetryPacket.put("Target Angle", targetAngle);
            telemetryPacket.put("Heading Error", error);
            telemetryPacket.put("Turn Power", turnPower);

            dashboard.sendTelemetryPacket(telemetryPacket);
        }
    }
    private void setDrivePower(double leftPower, double rightPower){
        leftFront.setPower(leftPower);
        leftBack.setPower(leftPower);
        rightFront.setPower(rightPower);
        rightBack.setPower(rightPower);
    }
}



