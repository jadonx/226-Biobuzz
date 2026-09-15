package org.firstinspires.ftc.teamcode.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandScheduler {

    private final Set<Command> scheduledCommands = new HashSet<>();

    private final Map<Subsystem, Command> requirements = new HashMap<>();

    private final Set<Subsystem> subsystems = new HashSet<>();


    /*
     * -------------------------
     * SUBSYSTEMS
     * -------------------------
     */

    public void registerSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }


    /*
     * -------------------------
     * SCHEDULING
     * -------------------------
     */

    public void schedule(Command command) {

        // Don't schedule the same command twice
        if (scheduledCommands.contains(command)) {
            return;
        }

        /*
         * Cancel commands that are currently using
         * any subsystem required by the new command.
         */
        for (Subsystem subsystem : command.getRequirements()) {

            Command occupyingCommand = requirements.get(subsystem);

            if (occupyingCommand != null) {
                cancel(occupyingCommand);
            }
        }

        /*
         * Claim the required subsystems.
         */
        for (Subsystem subsystem : command.getRequirements()) {
            requirements.put(subsystem, command);
        }

        scheduledCommands.add(command);

        command.init();
    }


    /*
     * -------------------------
     * MAIN LOOP
     * -------------------------
     */

    public void run() {

        /*
         * First update all subsystems.
         */
        for (Subsystem subsystem : subsystems) {
            subsystem.update();
        }

        /*
         * Copy the set so commands can safely be
         * removed while we're iterating.
         */
        for (Command command :
                new ArrayList<>(scheduledCommands)) {

            command.update();

            if (command.isFinished()) {
                finishCommand(command, false);
            }
        }
    }


    /*
     * -------------------------
     * CANCELLATION
     * -------------------------
     */

    public void cancel(Command command) {

        if (!scheduledCommands.contains(command)) {
            return;
        }

        finishCommand(command, true);
    }


    public void cancelAll() {

        for (Command command :
                new ArrayList<>(scheduledCommands)) {

            cancel(command);
        }
    }


    /*
     * -------------------------
     * INTERNAL
     * -------------------------
     */

    private void finishCommand(
            Command command,
            boolean interrupted) {

        command.finish(interrupted);

        scheduledCommands.remove(command);

        /*
         * Release the subsystems owned by this command.
         */
        for (Subsystem subsystem : command.getRequirements()) {

            if (requirements.get(subsystem) == command) {
                requirements.remove(subsystem);
            }
        }
    }


    /*
     * -------------------------
     * INFORMATION
     * -------------------------
     */

    public boolean isScheduled(Command command) {
        return scheduledCommands.contains(command);
    }
}