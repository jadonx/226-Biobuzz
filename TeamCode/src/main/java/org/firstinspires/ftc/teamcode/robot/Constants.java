package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Constants {
    // Hardware Map
    public static String driveMotorFL = "frontLeft";
    public static String driveMotorFR = "frontRight";
    public static String driveMotorBL = "backLeft";
    public static String driveMotorBR = "backRight";

    public static String imu = "imu";
    public static String pinpoint = "pinpoint";

    public static String shooterMotor1 = "shooter";

    public static String intakeMotor = "intake";

    // Values
    public static Double pinpointXOffset = 54.5;
    public static Double pinpointYOffset = -131.4;

    public static Pose2D redGoal1 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
    public static Pose2D redGoal2 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
    public static Pose2D blueGoal1 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
    public static Pose2D blueGOal2 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);

    public static enum AllianceColor {
            RED, BLUE
    }
}
