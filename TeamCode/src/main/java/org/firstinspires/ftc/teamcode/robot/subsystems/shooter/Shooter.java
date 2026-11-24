package org.firstinspires.ftc.teamcode.robot.subsystems.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.utils.Subsystem;

@Config
public class Shooter implements Subsystem {
    private DcMotorEx shooter;

    public static double kS = 0.15;
    public static double kV = 0.000415;
    public static double kP = 0;

    private double targetVelocity;

    public Shooter(HardwareMap hardwareMap) {
        shooter = hardwareMap.get(DcMotorEx.class, Constants.shooterMotor);

        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void update() {
        if (targetVelocity <= 0) {
            shooter.setPower(0);
            return;
        }

        double error = targetVelocity - shooter.getVelocity();

        double ff = kS + kV * targetVelocity;
        double power = ff + kP * error;

        shooter.setPower(Math.max(0, Math.min(power, 1)));
    }

//    public void update() {
//        double error = targetVelocity - shooter.getVelocity();
//
//        double ff = kS * Math.signum(targetVelocity) + kV * targetVelocity;
//
//        double p = kP * error;
//
//        double result = Math.max(-1.0, Math.min(ff + p, 1.0)); // clamp(value: ff + p, min: -1.0, max: 1.0)
//
//        shooter.setPower(result);
//    }
//
//    private double clamp(double value, double min, double max) {
//        return Math.max(min, Math.min(value, max));
//    }


    public void updateBB() {
        if (targetVelocity <= 0) {
            shooter.setPower(0);
            return;
        }

        double currentVelocity = shooter.getVelocity();

        double ff = kS + kV * targetVelocity;

        if (currentVelocity < targetVelocity) {
            shooter.setPower(1.0);
        } else {
            shooter.setPower(Math.max(0, Math.min(ff, 1.0)));
        }
    }



//    public static double tolerance = 20;
//
//    private boolean accelerating = true;
//
//    public void updateBB() {
//        if (targetVelocity <= 0) {
//            shooter.setPower(0);
//            accelerating = true;
//            return;
//        }
//
//        double currentVelocity = shooter.getVelocity();
//        double ff = kS + kV * targetVelocity;
//
//        if (currentVelocity < targetVelocity - tolerance) {
//            accelerating = true;
//        } else if (currentVelocity > targetVelocity + tolerance) {
//            accelerating = false;
//        }
//
//        double power = accelerating ? 1.0 : ff;
//
//        shooter.setPower(Math.max(0, Math.min(power, 1.0)));
//    }



    public double getCurrentRPM() {
        return shooter.getVelocity() / Constants.ticksPerRev * 60;
    }

    public void setTargetRPM(double targetRPM) {
        targetVelocity = targetRPM * Constants.ticksPerRev / 60;
    }

    public double getTargetRPM() {
        return targetVelocity / Constants.ticksPerRev * 60;
    }

    public double getCurrent() {
        return shooter.getCurrent(CurrentUnit.AMPS);
    }
}
