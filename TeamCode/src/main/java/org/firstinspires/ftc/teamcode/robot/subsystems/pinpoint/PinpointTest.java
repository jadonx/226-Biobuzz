package org.firstinspires.ftc.teamcode.robot.subsystems.pinpoint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.auto.roadrunner.Drawing;

@TeleOp(name="PinpointTest", group="Test")
public class PinpointTest extends OpMode {
    Pinpoint pinpoint;

    FtcDashboard dashboard;
    TelemetryPacket packet;

    @Override
    public void init() {
        pinpoint = new Pinpoint(hardwareMap);

        dashboard = FtcDashboard.getInstance();
        packet = new TelemetryPacket();
    }

    @Override
    public void loop() {
        pinpoint.update();

        Pose2D pos = pinpoint.getPosition();

        packet.fieldOverlay().setStroke("#3F51B5");
        Drawing.drawRobot(packet.fieldOverlay(), new Pose2d(pos.getX(DistanceUnit.INCH), pos.getY(DistanceUnit.INCH), pos.getHeading(AngleUnit.RADIANS)));

        dashboard.sendTelemetryPacket(packet);
    }
}
