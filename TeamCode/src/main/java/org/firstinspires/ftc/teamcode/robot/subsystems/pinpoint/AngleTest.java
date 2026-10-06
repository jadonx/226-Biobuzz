package org.firstinspires.ftc.teamcode.robot.subsystems.pinpoint;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.robot.Constants;

// import com.acmerobotics.dashboard.FtcDashboard;

@TeleOp(name="AngleTest")
public class AngleTest extends OpMode {
    private GoBildaPinpointDriver pinpoint;

    private final Pose2D targetPosition = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);

    @Override
    public void init() {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.pinpoint);

        pinpoint.setOffsets(Constants.pinpointXOffset, Constants.pinpointYOffset, DistanceUnit.MM);

        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        pinpoint.resetPosAndIMU();

        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
    }

    @Override
    public void loop() {
        pinpoint.update();

        Pose2D pose2D = pinpoint.getPosition();

        double currentX = pose2D.getX(DistanceUnit.INCH);
        double currentY = pose2D.getY(DistanceUnit.INCH);
        double currentAngle = pose2D.getHeading(AngleUnit.DEGREES);

        double targetX = targetPosition.getX(DistanceUnit.INCH);
        double targetY = targetPosition.getY(DistanceUnit.INCH);

        double targetAngleRadians = Math.atan2(targetY - currentY, targetX - currentX);
        double targetAngleDegrees = Math.toDegrees(targetAngleRadians);
        double angleError = targetAngleDegrees - currentAngle;

        telemetry.addData("X coordinate (IN)", -pose2D.getY(DistanceUnit.INCH));
        telemetry.addData("Y coordinate (IN)", pose2D.getX(DistanceUnit.INCH));
        telemetry.addData("Heading angle (DEGREES)", currentAngle);
        telemetry.addData("Degrees needed to Turn", angleError);
        telemetry.update();
    }
}