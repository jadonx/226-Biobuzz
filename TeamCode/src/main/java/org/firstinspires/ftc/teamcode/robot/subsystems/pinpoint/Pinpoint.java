package org.firstinspires.ftc.teamcode.robot.subsystems.pinpoint;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.utils.Subsystem;

@Config
public class Pinpoint implements Subsystem {
    private GoBildaPinpointDriver pinpoint;

    public Pinpoint(HardwareMap hardwareMap) {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.pinpoint);

        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);

        pinpoint.setOffsets(Constants.pinpointXOffset, Constants.pinpointYOffset, DistanceUnit.MM);

        pinpoint.resetPosAndIMU();
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
    }

    public void update() {
        pinpoint.update();
    }

    public double getPosX() {
        return pinpoint.getPosX(DistanceUnit.INCH);
    }

    public double getPosY() {
        return pinpoint.getPosY(DistanceUnit.INCH);
    }
}