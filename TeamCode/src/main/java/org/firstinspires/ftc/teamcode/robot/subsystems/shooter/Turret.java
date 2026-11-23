package org.firstinspires.ftc.teamcode.robot.subsystems.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Config
public class Turret {
    // TODO: enter total num of ticks for full 360 rotation
    public static double totalTicks = 1050.0; // adjust
    public static double TPD = totalTicks / 360.0;//ticks per degree

    public static double kP = 0.008; // adjust
    public static double kF = 0.05;
    public static double TOLERANCE_DEGREES = 0.5;


    private DcMotorEx turret;
    private double targetAngle = 0.0;

    public Turret(HardwareMap hardwareMap) {
        turret = hardwareMap.get(DcMotorEx.class, "turret");
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    public void update() {
        int currentTicks = turret.getCurrentPosition();
        double currentAngle = currentTicks / TPD;

        // normalize targetAngle to [-180, 180]
        double safeTarget = targetAngle;
        while (safeTarget > 180) {
            safeTarget -= 360;
        }
        while (safeTarget <= -180) {
            safeTarget += 360;
        }
        // forces rotation back through 0 instead of crossing ±180
        double error = safeTarget - currentAngle;

        double motorPower;
        if (Math.abs(error) <= TOLERANCE_DEGREES) {
            motorPower = 0.0;
        } else {
            motorPower = (error * kP) + (Math.signum(error) * kF);
        }

        motorPower = Math.max(-1.0, Math.min(1.0, motorPower));
        // Hard-stop failsafe to cut outward power at the +-180 deg limits
        if ((currentAngle >= 180.0 && motorPower > 0) || (currentAngle <= -180.0 && motorPower < 0)) {
            motorPower = 0;
        }
        turret.setPower(motorPower);
    }



    // Getters and Setters
    public void setTargetAngle(double angle) {
        this.targetAngle = angle;
    }

    public double getTargetAngle() {
        return targetAngle;
    }

    public double getCurrentAngle() {
        return turret.getCurrentPosition()/TPD;
    }

    public int getRawTicks() {
        return turret.getCurrentPosition();
    }
}