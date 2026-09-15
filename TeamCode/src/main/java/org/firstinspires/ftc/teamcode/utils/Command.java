package org.firstinspires.ftc.teamcode.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Command {

    private final Set<Subsystem> requirements = new HashSet<>();

    public Command(Subsystem... requirements) {
        this.requirements.addAll(Arrays.asList(requirements));
    }

    /**
     * Called once when the command starts.
     */
    public void init() {}

    /**
     * Called repeatedly while the command is scheduled.
     */
    public abstract void update();

    /**
     * Returns true when the command should stop.
     */
    public abstract boolean isFinished();

    /**
     * Called once when the command ends.
     *
     * interrupted = false:
     *     Command finished normally.
     *
     * interrupted = true:
     *     Command was cancelled or replaced by another command.
     */
    public void finish(boolean interrupted) {}

    public Set<Subsystem> getRequirements() {
        return requirements;
    }
}