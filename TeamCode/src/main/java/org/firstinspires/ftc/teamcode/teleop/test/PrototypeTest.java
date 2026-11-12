package org.firstinspires.ftc.teamcode.teleop.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import org.firstinspires.ftc.teamcode.auto.roadrunner.Drawing;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp(name="PrototypeTest", group="Test")
public class PrototypeTest extends OpMode {
    Robot robot;

    FtcDashboard dashboard;
    TelemetryPacket packet;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, gamepad1);

        dashboard = FtcDashboard.getInstance();
        packet = new TelemetryPacket();
    }

    @Override
    public void loop() {
        robot.update();

        packet.put("Left Stick X", gamepad1.left_stick_x);
        packet.put("Left Stick Y", -gamepad1.left_stick_y);
        packet.put("Right Stick X", gamepad1.right_stick_x);
        packet.put("Left Trigger", gamepad1.left_trigger);
        packet.put("Right Trigger", gamepad1.right_trigger);

        packet.put("Front Left Power", robot.getFrontLeft());
        packet.put("Front Right Power", robot.getFrontRight());
        packet.put("Back Left Power", robot.getBackLeft());
        packet.put("Back Right Power", robot.getBackRight());

        packet.put("Heading", robot.getHeadingDegrees());

        double rotX = gamepad1.left_stick_x * Math.cos(-robot.getHeadingRadians()) - -gamepad1.left_stick_y * Math.sin(-robot.getHeadingRadians());
        double rotY = gamepad1.left_stick_x * Math.sin(-robot.getHeadingRadians()) + -gamepad1.left_stick_y * Math.cos(-robot.getHeadingRadians());

        packet.put("rotX", rotX);
        packet.put("rotY", rotY);
        packet.put("denominator", Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(gamepad1.right_stick_x), 1));

        dashboard.sendTelemetryPacket(packet);
    }
}
