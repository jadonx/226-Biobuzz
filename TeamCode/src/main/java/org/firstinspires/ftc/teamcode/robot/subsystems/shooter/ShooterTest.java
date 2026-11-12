package org.firstinspires.ftc.teamcode.robot.subsystems.shooter;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp(name="Shooter Test", group="Test")
public class ShooterTest extends OpMode {
    Shooter shooter;

    public static double targetRPM;

    FtcDashboard dashboard;
    TelemetryPacket packet;

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap);

        dashboard = FtcDashboard.getInstance();
        packet = new TelemetryPacket();
    }

    @Override
    public void loop() {
        shooter.setTargetRPM(targetRPM);
        shooter.update();

        packet.put("Current RPM", shooter.getCurrentRPM());
        packet.put("Target RPM", shooter.getTargetRPM());

        dashboard.sendTelemetryPacket(packet);
    }
}
