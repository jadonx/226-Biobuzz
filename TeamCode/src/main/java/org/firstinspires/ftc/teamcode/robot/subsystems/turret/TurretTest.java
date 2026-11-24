package org.firstinspires.ftc.teamcode.robot.subsystems.turret;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp(name = "TurretTest")
public class TurretTest extends OpMode {

    public static double targetAngle = 0.0;
    public static double kP = 0.002;

    private Turret turret;
    private FtcDashboard dashboard;

    @Override
    public void init() {
        turret = new Turret(hardwareMap);
        dashboard = FtcDashboard.getInstance();
    }

    @Override
    public void loop() {
        Turret.kP = kP; // overwrite the subsystem kP with Dashboard value
        turret.setTargetAngle(targetAngle);

        turret.update();

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("Target Angle", targetAngle);
        packet.put("Current Angle", turret.getCurrentAngle());
        packet.put("Raw Ticks", turret.getRawTicks());
        dashboard.sendTelemetryPacket(packet);

        telemetry.addData("Target Angle (DEG)", targetAngle);
        telemetry.addData("Current Angle (DEG)", turret.getCurrentAngle());
        telemetry.addData("Raw Ticks", turret.getRawTicks());
        telemetry.update();
    }
}