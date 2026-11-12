package org.firstinspires.ftc.teamcode.teleop.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="GamepadTest", group="Test")
public class GamepadTest extends OpMode {
    FtcDashboard dashboard;
    TelemetryPacket packet;

    @Override
    public void init() {
        dashboard = FtcDashboard.getInstance();
        packet = new TelemetryPacket();
    }

    @Override
    public void loop() {
        packet.put("Left Stick X", gamepad1.left_stick_x);
        packet.put("Left Stick Y", -gamepad1.left_stick_y);
        packet.put("Right Stick X", gamepad1.right_stick_x);
        packet.put("Left Trigger", gamepad1.left_trigger);
        packet.put("Right Trigger", gamepad1.right_trigger);

        dashboard.sendTelemetryPacket(packet);
    }
}
