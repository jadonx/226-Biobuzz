package org.firstinspires.ftc.teamcode.example_code;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.utils.Command;

@Disabled
public class MoveArmCommand extends Command {
    private final Arm arm;
    private final double targetPosition;

    public MoveArmCommand(Arm arm, double targetPosition) {
        super(arm);

        this.arm = arm;
        this.targetPosition = targetPosition;
    }

    @Override
    public void init() {
        // Runs ONCE when the command starts
        arm.setTargetPosition(targetPosition);
    }

    @Override
    public void update() {
        // Nothing needed here!
        // Arm.update() handles moving the motor.
    }

    @Override
    public boolean isFinished() {
        // Scheduler keeps running the command
        // until the arm reaches its target.
        return arm.atTarget();
    }

    @Override
    public void finish(boolean interrupted) {
        // Nothing needed here for this command.
    }
}
