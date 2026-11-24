package org.firstinspires.ftc.teamcode.robot.subsystems.flowerintake;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robot.Constants;

import java.util.ArrayList;
import java.util.List;

@Config
@TeleOp(name="FlowerIntakeTest", group="Test")
public class FlowerIntakeTest extends OpMode {
    CRServo flowerIntake;
    LynxModule expansionHub;
    DcMotorEx intake;
    DcMotorEx transfer;
    DcMotorEx shooter;

    public static double intakeMotorPower;
    public static double transferMotorPower;
    public static double shooterMotorPower;

    List<Double> currents = new ArrayList<>();
    double average;
    boolean flowerIntakeUp;
    boolean flowerIntakeDown;

    FtcDashboard dashboard;
    TelemetryPacket packet;

    @Override
    public void init() {
        flowerIntake = hardwareMap.get(CRServo.class, Constants.flowerIntakeMotor);
        flowerIntake.setDirection(DcMotorSimple.Direction.FORWARD);

        expansionHub = hardwareMap.get(LynxModule.class, "Expansion Hub 2");

        intake = hardwareMap.get(DcMotorEx.class, Constants.intakeMotor);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        transfer = hardwareMap.get(DcMotorEx.class, Constants.transferMotor);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter = hardwareMap.get(DcMotorEx.class, Constants.shooterMotor);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        dashboard = FtcDashboard.getInstance();
        packet = new TelemetryPacket();
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            flowerIntakeUp = !flowerIntakeUp;
            flowerIntakeDown = false;
        }
        if (gamepad1.bWasPressed()) {
            flowerIntakeDown = !flowerIntakeDown;
            flowerIntakeUp = false;
        }

        double current = expansionHub.getCurrent(CurrentUnit.AMPS) - intake.getCurrent(CurrentUnit.AMPS) - transfer.getCurrent(CurrentUnit.AMPS) - shooter.getCurrent(CurrentUnit.AMPS);

        if (currents.size() == 5) {
            currents.remove(0);
        }
        currents.add(current);

        if (flowerIntakeUp) {
            flowerIntake.setPower(-1.0);
        }
        if (flowerIntakeDown) {
            flowerIntake.setPower(1.0);
        }

        if (average >= 0.6) {
            flowerIntakeUp = false;
            flowerIntakeDown = false;
            flowerIntake.setPower(0.0);
        }

//        flowerIntake.setPower(gamepad1.right_stick_y); // stick up -> flower down

        intake.setPower(intakeMotorPower);
        transfer.setPower(transferMotorPower);
        shooter.setPower(shooterMotorPower);

//        packet.put("Expansion Hub Current", expansionHub.getCurrent(CurrentUnit.AMPS));
//        packet.put("Flower Intake Current", expansionHub.getCurrent(CurrentUnit.AMPS) - intake.getCurrent(CurrentUnit.AMPS) - transfer.getCurrent(CurrentUnit.AMPS) - shooter.getCurrent(CurrentUnit.AMPS));
//        packet.put("Intake Current", intake.getCurrent(CurrentUnit.AMPS));
//        packet.put("Transfer Current", transfer.getCurrent(CurrentUnit.AMPS));
//        packet.put("Shooter Current", shooter.getCurrent(CurrentUnit.AMPS));

        if (currents.size() == 5) average = (currents.get(0) + currents.get(1) + currents.get(2) + currents.get(3) + currents.get(4)) / 5;
        packet.put("Average Current", average);
        packet.put("Current Spike", average >= 0.5);
        packet.put("Flower Intake Up", flowerIntakeUp);
        packet.put("Flower Intake Down", flowerIntakeDown);
        dashboard.sendTelemetryPacket(packet);
    }
}


//
//
//package org.firstinspires.ftc.teamcode.robot.subsystems.flowerintake;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.qualcomm.hardware.lynx.LynxModule;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
//import org.firstinspires.ftc.teamcode.robot.Constants;
//
//@Config
//@TeleOp(name = "FlowerIntakeTest", group = "Test")
//public class FlowerIntakeTest extends OpMode {
//
//    CRServo flowerIntake;
//    LynxModule expansionHub;
//
//    DcMotorEx intake;
//    DcMotorEx transfer;
//    DcMotorEx shooter;
//
//    // Motor powers
//    public static double intakeMotorPower = 0;
//    public static double transferMotorPower = 0;
//    public static double shooterMotorPower = 0;
//
//    // Flower intake settings
//    public static double flowerIntakePower = 1.0;
//    public static double joystickDeadband = 0.05;
//
//    // Current filtering
//    public static double filterTimeConstantMs = 60;
//
//    private double rawCurrent = 0;
//    private double filteredCurrent = 0;
//
//    private boolean filterInitialized = false;
//
//    // Stall detection settings
//    public static boolean enableStallDetection = true;
//
//    public static double positiveStallCurrent = 2.0;
//    public static double negativeStallCurrent = 2.0;
//
//    public static double stallTimeMs = 100;
//    public static double startupDelayMs = 150;
//
//    // Backup safety timeout
//    public static double maxMovementTimeMs = 1500;
//
//    // Timers
//    private final ElapsedTime stallTimer = new ElapsedTime();
//    private final ElapsedTime movementTimer = new ElapsedTime();
//    private final ElapsedTime loopTimer = new ElapsedTime();
//
//    // Stall detection states
//    private boolean timingStall = false;
//
//    private int blockedDirection = 0;
//    private int previousDirection = 0;
//
//    private String stopReason = "NONE";
//
//    FtcDashboard dashboard;
//    TelemetryPacket packet;
//
//    @Override
//    public void init() {
//
//        flowerIntake = hardwareMap.get(
//                CRServo.class,
//                Constants.flowerIntakeMotor
//        );
//
//        flowerIntake.setDirection(
//                DcMotorSimple.Direction.FORWARD
//        );
//
//        flowerIntake.setPower(0);
//
//        expansionHub = hardwareMap.get(
//                LynxModule.class,
//                "Expansion Hub 2"
//        );
//
//        intake = hardwareMap.get(
//                DcMotorEx.class,
//                Constants.intakeMotor
//        );
//
//        intake.setDirection(
//                DcMotorSimple.Direction.REVERSE
//        );
//
//        intake.setZeroPowerBehavior(
//                DcMotor.ZeroPowerBehavior.BRAKE
//        );
//
//        transfer = hardwareMap.get(
//                DcMotorEx.class,
//                Constants.transferMotor
//        );
//
//        transfer.setZeroPowerBehavior(
//                DcMotor.ZeroPowerBehavior.BRAKE
//        );
//
//        shooter = hardwareMap.get(
//                DcMotorEx.class,
//                Constants.shooterMotor
//        );
//
//        shooter.setMode(
//                DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        );
//
//        shooter.setZeroPowerBehavior(
//                DcMotor.ZeroPowerBehavior.FLOAT
//        );
//
//        shooter.setDirection(
//                DcMotorSimple.Direction.FORWARD
//        );
//
//        dashboard = FtcDashboard.getInstance();
//
//        blockedDirection = 0;
//        previousDirection = 0;
//        timingStall = false;
//        filterInitialized = false;
//    }
//
//    @Override
//    public void start() {
//        loopTimer.reset();
//        movementTimer.reset();
//        stallTimer.reset();
//    }
//
//    @Override
//    public void loop() {
//
//        // ==========================================
//        // 1. CONTROL OTHER MOTORS
//        // ==========================================
//
//        intake.setPower(intakeMotorPower);
//        transfer.setPower(transferMotorPower);
//        shooter.setPower(shooterMotorPower);
//
//        // ==========================================
//        // 2. READ CURRENT
//        // ==========================================
//
//        double hubCurrent =
//                expansionHub.getCurrent(CurrentUnit.AMPS);
//
//        double intakeCurrent =
//                intake.getCurrent(CurrentUnit.AMPS);
//
//        double transferCurrent =
//                transfer.getCurrent(CurrentUnit.AMPS);
//
//        double shooterCurrent =
//                shooter.getCurrent(CurrentUnit.AMPS);
//
//        // Estimate flower intake current
//        rawCurrent = hubCurrent
//                - intakeCurrent
//                - transferCurrent
//                - shooterCurrent;
//
//        // ==========================================
//        // 3. EXPONENTIAL MOVING AVERAGE
//        // ==========================================
//
//        double dt = loopTimer.milliseconds();
//        loopTimer.reset();
//
//        double alpha = 1.0 - Math.exp(
//                -dt / Math.max(filterTimeConstantMs, 1.0)
//        );
//
//        if (!filterInitialized) {
//
//            filteredCurrent = rawCurrent;
//            filterInitialized = true;
//
//        } else {
//
//            filteredCurrent += alpha
//                    * (rawCurrent - filteredCurrent);
//        }
//
//        // ==========================================
//        // 4. GAMEPAD INPUT
//        // ==========================================
//
//        double requestedPower = gamepad1.right_stick_y;
//
//        if (Math.abs(requestedPower) < joystickDeadband) {
//            requestedPower = 0;
//        }
//
//        requestedPower *= flowerIntakePower;
//
//        requestedPower = Math.max(
//                -1.0,
//                Math.min(1.0, requestedPower)
//        );
//
//        int direction = (int) Math.signum(requestedPower);
//
//        // Reset timers when starting or changing direction
//        if (direction != previousDirection) {
//
//            movementTimer.reset();
//            stallTimer.reset();
//
//            timingStall = false;
//        }
//
//        // ==========================================
//        // 5. DIRECTION LOCK
//        // ==========================================
//
//        double outputPower = requestedPower;
//
//        if (direction != 0 && direction == blockedDirection) {
//            outputPower = 0;
//        }
//
//        // Allow movement away from a blocked direction
//        if (direction != 0 && direction == -blockedDirection) {
//
//            blockedDirection = 0;
//            stopReason = "NONE";
//        }
//
//        // Select current threshold based on direction
//        double currentThreshold = direction >= 0
//                ? positiveStallCurrent
//                : negativeStallCurrent;
//
//        // ==========================================
//        // 6. CURRENT-BASED STALL DETECTION
//        // ==========================================
//
//        if (enableStallDetection
//                && outputPower != 0
//                && movementTimer.milliseconds() >= startupDelayMs) {
//
//            if (filteredCurrent >= currentThreshold) {
//
//                // Start confirmation timer
//                if (!timingStall) {
//
//                    stallTimer.reset();
//                    timingStall = true;
//                }
//
//                // Confirm sustained high current
//                if (stallTimer.milliseconds() >= stallTimeMs) {
//
//                    blockedDirection = direction;
//
//                    outputPower = 0;
//                    timingStall = false;
//
//                    stopReason = "CURRENT STALL";
//                }
//
//            } else {
//
//                // Current returned to normal
//                timingStall = false;
//                stallTimer.reset();
//            }
//
//        } else {
//
//            timingStall = false;
//            stallTimer.reset();
//        }
//
//        // ==========================================
//        // 7. MAXIMUM MOVEMENT TIME PROTECTION
//        // ==========================================
//
//        if (outputPower != 0
//                && movementTimer.milliseconds() >= maxMovementTimeMs) {
//
//            blockedDirection = direction;
//
//            outputPower = 0;
//            timingStall = false;
//
//            stopReason = "MOVEMENT TIMEOUT";
//        }
//
//        // ==========================================
//        // 8. APPLY SERVO POWER
//        // ==========================================
//
//        flowerIntake.setPower(outputPower);
//
//        previousDirection = direction;
//
//        // ==========================================
//        // 9. DASHBOARD TELEMETRY
//        // ==========================================
//
//        packet = new TelemetryPacket();
//
//        // Current measurements
//        packet.put("Expansion Hub Current", hubCurrent);
//        packet.put("Intake Current", intakeCurrent);
//        packet.put("Transfer Current", transferCurrent);
//        packet.put("Shooter Current", shooterCurrent);
//
//        packet.put("Flower Intake Raw Current", rawCurrent);
//        packet.put("Flower Intake Filtered Current", filteredCurrent);
//        packet.put("Stall Current Threshold", currentThreshold);
//
//        // Servo control
//        packet.put("Requested Power", requestedPower);
//        packet.put("Output Power", outputPower);
//
//        // Stall detection
//        packet.put("Blocked Direction", blockedDirection);
//        packet.put("Timing Stall", timingStall);
//        packet.put("Stop Reason", stopReason);
//
//        packet.put(
//                "Stall Timer (ms)",
//                timingStall ? stallTimer.milliseconds() : 0
//        );
//
//        packet.put(
//                "Movement Timer (ms)",
//                movementTimer.milliseconds()
//        );
//
//        // Filter information
//        packet.put("Loop Time (ms)", dt);
//        packet.put("Filter Alpha", alpha);
//
//        dashboard.sendTelemetryPacket(packet);
//    }
//
//    @Override
//    public void stop() {
//
//        flowerIntake.setPower(0);
//
//        intake.setPower(0);
//        transfer.setPower(0);
//        shooter.setPower(0);
//    }
//}
