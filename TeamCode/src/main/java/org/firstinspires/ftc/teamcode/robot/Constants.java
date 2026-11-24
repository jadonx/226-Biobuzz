package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Constants {
    // Hardware Map
    public static String driveMotorFL = "frontLeft"; // 0 control hub
    public static String driveMotorFR = "frontRight"; // 1 control hub
    public static String driveMotorBL = "backLeft"; // 2 control hub
    public static String driveMotorBR = "backRight"; // 3 control hub

    public static String imu = "imu";

    public static String pinpoint = "pinpoint";
    public static String limelight = "limelight";
    public static String shooterMotor = "shooter"; // 0 expansion
    public static String intakeMotor = "intake"; // 3 expansion
    public static String transferMotor = "transfer"; // 2 expansion
    public static String turretMotor = "turret"; // 1 expansion

    public static String flowerIntakeMotor = "flowerIntake";

    public static String expansionHub = "Expansion Hub 2";

    // Values
    public static Double ticksPerRev = 28.0;

    public static Double pinpointXOffset = 54.5;
    public static Double pinpointYOffset = -130.0;

    public static Pose2D redGoal1 = new Pose2D(DistanceUnit.INCH, 15, -15, AngleUnit.DEGREES, 0);
    public static Pose2D redGoal2 = new Pose2D(DistanceUnit.INCH, -15, -15, AngleUnit.DEGREES, 0);
    public static Pose2D blueGoal1 = new Pose2D(DistanceUnit.INCH, 17, 15, AngleUnit.DEGREES, 0);
    public static Pose2D blueGoal2 = new Pose2D(DistanceUnit.INCH, -1, 15, AngleUnit.DEGREES, 0);
//    public static Pose2D redGoal1 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
//    public static Pose2D redGoal2 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
//    public static Pose2D blueGoal1 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
//    public static Pose2D blueGoal2 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);

    public static enum AllianceColor {
        RED, BLUE
    }
}