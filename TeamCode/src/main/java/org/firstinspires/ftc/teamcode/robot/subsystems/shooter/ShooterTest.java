package org.firstinspires.ftc.teamcode.robot.subsystems.shooter;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot.Constants;

@Config
@TeleOp(name="Shooter Test", group="Test")
public class ShooterTest extends OpMode {
    Shooter shooter;
    DcMotorEx intake;
    DcMotorEx transfer;
    public static double intakeMotorPower;
    public static double transferMotorPower;

    public static double targetRPM;

    FtcDashboard dashboard;
    TelemetryPacket packet;

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap);

        intake = hardwareMap.get(DcMotorEx.class, Constants.intakeMotor);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        transfer = hardwareMap.get(DcMotorEx.class, Constants.transferMotor);
        transfer.setDirection(DcMotorSimple.Direction.FORWARD);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        dashboard = FtcDashboard.getInstance();
        packet = new TelemetryPacket();
    }

    @Override
    public void loop() {
        shooter.setTargetRPM(targetRPM);
        shooter.update();

        intake.setPower(intakeMotorPower);
        transfer.setPower(transferMotorPower);

        packet.put("Current RPM",-shooter.getCurrentRPM());
        packet.put("Target RPM", shooter.getTargetRPM());

        dashboard.sendTelemetryPacket(packet);
    }
}
