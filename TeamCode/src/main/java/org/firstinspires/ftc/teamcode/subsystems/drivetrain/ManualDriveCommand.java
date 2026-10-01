package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.utils.Command;

public class ManualDriveCommand extends Command {

    private final Drivetrain drivetrain;
    private final Gamepad gamepad;

    public ManualDriveCommand(Drivetrain drivetrain, Gamepad gamepad) {
        this.drivetrain = drivetrain;
        this.gamepad = gamepad;
    }

    @Override
    public void update() {
        double x = gamepad.left_stick_x;
        double y = -gamepad.left_stick_y;
        double rx = gamepad.right_stick_x;

        drivetrain.driveFieldCentric(x, y, rx);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void finish(boolean interrupted) {
        drivetrain.stop();
    }
}
