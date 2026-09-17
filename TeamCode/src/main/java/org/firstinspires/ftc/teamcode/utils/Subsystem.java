package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Subsystem {

    /**
     * Called once every robot loop.
     *
     * Put continuous subsystem logic here, such as:
     * - PID calculations
     * - motor power updates
     * - sensor processing
     * - state machines
     */
    void init();

    void update();
}