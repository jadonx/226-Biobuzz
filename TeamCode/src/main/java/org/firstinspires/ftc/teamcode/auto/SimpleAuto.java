package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.robot.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.transfer.Transfer;

@Autonomous(name="SimpleAuto", group="Autonomous")
public class SimpleAuto extends LinearOpMode {
    Drivetrain drivetrain;
    Intake intake;
    Transfer transfer;
    Shooter shooter;

    ElapsedTime timer = new ElapsedTime();


    @Override
    public void runOpMode() throws InternalError {
        drivetrain = new Drivetrain(hardwareMap);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        shooter = new Shooter(hardwareMap);

        waitForStart();
        if (isStopRequested()) return;

        drivetrain.setPower(0.2);
        sleep(1000);
        drivetrain.setPower(-0.1);
        sleep(1000);
        drivetrain.setPower(0);

        shooter.setTargetRPM(4500);

        timer.reset();

        while (opModeIsActive() && timer.seconds() < 10.0) {
            shooter.update();

            if (timer.seconds() > 5) {
                transfer.update(1);
                intake.update(1);
            }

            telemetry.addData("Time", timer.seconds());
            telemetry.update();

            idle();
        }

        intake.update(0);
        transfer.update(0);
        shooter.setTargetRPM(0);
    }
}
