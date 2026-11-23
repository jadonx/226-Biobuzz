package org.firstinspires.ftc.teamcode.robot.subsystems.shooter;


import org.firstinspires.ftc.teamcode.robot.subsystems.shooter.LimeLight;

public class TurretAimController {
    private final Turret turret;
    private final LimeLight limelight;

    // TRUE if Limelight is mounted ON the rotating turret; FALSE if fixed on the robot chassis
    private final boolean cameraMountedOnTurret;

    // Flip to -1.0 if your turret rotates AWAY from the target instead of toward it
    private static final double TX_SIGN = -1.0;

    private LimeLight.HiveCell targetCell = LimeLight.HiveCell.RED_AUDIENCE;

    public TurretAimController(Turret turret, LimeLight limelight, boolean cameraMountedOnTurret) {
        this.turret = turret;
        this.limelight = limelight;
        this.cameraMountedOnTurret = cameraMountedOnTurret;
    }

    public void setTargetCell(LimeLight.HiveCell cell) {
        this.targetCell = cell;
    }

    /**
     * Call this every loop in Autonomous or TeleOp.
     */
    public void update() {
        limelight.update();
        double tx = limelight.getCellTX(targetCell);

        if (!Double.isNaN(tx)) {
            // 1. LIMELIGHT SEES THE HIVE CELL
            double desiredAngle;
            desiredAngle = turret.getCurrentAngle() + (TX_SIGN * tx);
            turret.setTargetAngle(desiredAngle);

        } else {
            // 2. TARGET NOT VISIBLE (Pinpoint hook for later!)
            // Right now: hold last known targetAngle.
            // Later with Pinpoint:
            // double fieldAngleToCell = Math.toDegrees(Math.atan2(cellY - robotY, cellX - robotX));
            // turret.setTargetAngle(fieldAngleToCell - robotHeadingDeg);
        }

        // Always run your Turret's motor control loop
        turret.update();
    }

    /** Helpful check for Autonomous before firing Pollen/Nectar */
    public boolean isLockedOnTarget() {
        double tx = limelight.getCellTX(targetCell);
        boolean visionAligned = !Double.isNaN(tx) && Math.abs(tx) <= Turret.TOLERANCE_DEGREES;
        boolean motorSettled = Math.abs(turret.getTargetAngle() - turret.getCurrentAngle()) <= Turret.TOLERANCE_DEGREES;
        return visionAligned && motorSettled;
    }
}

