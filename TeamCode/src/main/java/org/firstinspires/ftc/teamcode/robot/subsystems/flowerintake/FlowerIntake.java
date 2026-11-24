package org.firstinspires.ftc.teamcode.robot.subsystems.flowerintake;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.utils.Subsystem;

import java.util.ArrayList;
import java.util.List;

public class FlowerIntake implements Subsystem {
    CRServo flowerIntake;

    List<Double> currents = new ArrayList<>();
    double average = 0.0;

    boolean moving = false;

    public FlowerIntake(HardwareMap hardwareMap) {
        flowerIntake = hardwareMap.get(CRServo.class, Constants.flowerIntakeMotor);
        flowerIntake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public boolean update(double power, double current) {
        if (power == 0.0) {
            flowerIntake.setPower(0.0);
            moving = false;
            currents.clear();
            return false;
        }

        if (!moving) {
            moving = true;
            currents.clear();
        }

        if (currents.size() >= 5) {
            currents.remove(0);
        }
        currents.add(current);

        double sum = 0.0;
        for (int i = 0; i < currents.size(); i++) {
            sum += currents.get(i);
        }
        average = sum / currents.size();

        if (average >= 0.6) {
            flowerIntake.setPower(0.0);
            moving = false;
            currents.clear();
            return true;
        }

        flowerIntake.setPower(power);

        return false;
    }
}
