package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.robot.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.robot.utils.CommandScheduler;

@TeleOp(name="PrototypeV1Test", group="Test")
public class PrototypeV1Test extends OpMode {
    Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, gamepad1, gamepad2, Constants.AllianceColor.BLUE);
    }

    @Override
    public void loop() {
        robot.update();
    }
}
