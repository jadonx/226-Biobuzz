package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class ElevatorProblem extends OpMode {
    DcMotor elevatorMotor;

    @Override
    public void init() {
        elevatorMotor = hardwareMap.get(DcMotorEx.class, "");
    }

    @Override
    public void loop() {
        double target = 50; // Target position

        // Adam
        double elevatorPosition = elevatorMotor.getCurrentPosition();
        elevatorMotor.setPower(1-elevatorPosition/target);

        // Jadon
        double error = target - elevatorMotor.getCurrentPosition();
        elevatorMotor.setPower(error * 0.01); // 0.01 is just a constant that needs to be tuned for the system


        /*
        if(elevatorPosition<50){
            elevatorMotor.setPower(1);
        }
        else if(elevatorPosition>50){
            if(elevatorMotor.getPower()>0){
                elevatorMotor.setPower(elevatorMotor.getPower()/-2);
            }
            else
                elevatorMotor.setPower(elevatorMotor.getPower()/2);
        }
        else
            elevatorMotor.setPower(0);
         */

        /*
        if (elevatorPosition < 50) {
            elevatorMotor.setPower(0.1);
        }
        else if (elevatorPosition > 50) {
            elevatorMotor.setPower(-0.05);
        }
        else {
            elevatorMotor.setPower(0);
        }
         */
    }
}
