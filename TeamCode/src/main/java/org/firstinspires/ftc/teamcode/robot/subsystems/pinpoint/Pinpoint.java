package org.firstinspires.ftc.teamcode.robot.subsystems.pinpoint;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.utils.Subsystem;

public class Pinpoint implements Subsystem {
    private final GoBildaPinpointDriver pinpoint;

    public Pinpoint(HardwareMap hardwareMap) {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.pinpoint);

        pinpoint.setOffsets(Constants.pinpointXOffset, Constants.pinpointYOffset, DistanceUnit.MM);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED
        );

        pinpoint.resetPosAndIMU();
        setPose(0, 0, 0);
    }

    public void update() {
        pinpoint.update();
    }

    public void setOffsets(double xOffsetMM, double yOffsetMM) {
        pinpoint.setOffsets(xOffsetMM, yOffsetMM, DistanceUnit.MM);
    }

    public void setPose(double xInches, double yInches, double headingDegrees) {
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, xInches, yInches, AngleUnit.DEGREES, headingDegrees));
    }

    public void resetPosAndIMU() {
        pinpoint.resetPosAndIMU();
    }

    public Pose2D getPose() {
        return pinpoint.getPosition();
    }

    public double getX() {
        return pinpoint.getPosition().getX(DistanceUnit.INCH);
    }

    public double getY() {
        return pinpoint.getPosition().getY(DistanceUnit.INCH);
    }

    public double getHeading() {
        return pinpoint.getPosition().getHeading(AngleUnit.DEGREES);
    }

    public double getHeadingRadians() {
        return pinpoint.getPosition().getHeading(AngleUnit.RADIANS);
    }

    public double getDistanceTo(Pose2D target) {
        return Math.hypot(
                target.getX(DistanceUnit.INCH) - getX(),
                target.getY(DistanceUnit.INCH) - getY()
        );
    }

    public Pose2D getClosestGoal(Pose2D goal1, Pose2D goal2) {
        if (getDistanceTo(goal1) <= getDistanceTo(goal2)) {
            return goal1;
        } else {
            return goal2;
        }
    }

    public Pose2D getClosestBlueGoal() {
        return getClosestGoal(Constants.blueGoal1, Constants.blueGoal2);
    }

    public Pose2D getClosestRedGoal() {
        return getClosestGoal(Constants.redGoal1, Constants.redGoal2);
    }

    public double getAngleToRadians(Pose2D target) {
        double dx = target.getX(DistanceUnit.INCH) - getX();
        double dy = target.getY(DistanceUnit.INCH) - getY();
        return Math.atan2(dy, dx);
    }

    public double getAngleToDegrees(Pose2D target) {
        return Math.toDegrees(getAngleToRadians(target));
    }

    public double getAngleError(Pose2D target) {
        double error = getAngleToDegrees(target) - getHeading();
        while (error > 180) error -= 360;
        while (error <= -180) error += 360;
        return error;
    }
}
    /*package org.firstinspires.ftc.teamcode.robot.subsystems.pinpoint;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.utils.Subsystem;

public class Pinpoint implements Subsystem {
    private GoBildaPinpointDriver pinpoint;

    public Pinpoint(HardwareMap hardwareMap) {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.pinpoint);

        // TODO: Make sure these are correct!
        pinpoint.setOffsets(Constants.pinpointXOffset, Constants.pinpointYOffset, DistanceUnit.MM);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        // TODO: This one too!
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        pinpoint.resetPosAndIMU();
    }

    public void update() {
        // TODO: Update pinpoint
        pinpoint.update();
    }

    public Pose2D getPose() {
        // TODO: Return correct pose
        return pinpoint.getPosition();
    }
}

     */