package org.firstinspires.ftc.teamcode.robot.subsystems.shooter;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

@Config
public class LimeLight {
    public enum HiveCell {
        RED_FAR(30, 31, 32, 33),
        RED_AUDIENCE(34, 35, 36, 37),
        BLUE_AUDIENCE(38, 39, 40, 41),
        BLUE_FAR(42, 43, 44, 45);

        public final int[] tagIds;
        HiveCell(int... ids) {
            this.tagIds = ids;
        }

        public boolean contains(int id) {
            for (int tagId : tagIds) {
                if (tagId == id) return true;
            }
            return false;
        }
    }

    // Tune these in FTC Dashboard for your physical camera mount
    public static double CAM_DEG = 27.3;     // Camera upward tilt angle (degrees)
    public static double CAM_H = 11.5;       // Camera height from floor (inches)
    public static double TARGET_H = 29.5;    // Height of Hive Cell AprilTag cluster (inches)

    private final Limelight3A limelight;
    private LLResult latestResult;

    public LimeLight(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0); // Set to your 36h11 AprilTag pipeline index
        limelight.start();
    }

    /** Call once per loop in TeleOp / Auto */
    public void update() {
        latestResult = limelight.getLatestResult();
    }

    public boolean hasValidTarget() {
        return latestResult != null && latestResult.isValid();
    }

    /**
     * Returns the average horizontal angle (tX) to all visible tags in the chosen HiveCell.
     * Returns Double.NaN if none of that cell's tags are currently visible.
     */
    public double getCellTX(HiveCell targetCell) {
        if (!hasValidTarget()) return Double.NaN;

        List<LLResultTypes.FiducialResult> fiducials = latestResult.getFiducialResults();
        double sumTx = 0.0;
        int count = 0;

        for (LLResultTypes.FiducialResult fid : fiducials) {
            if (targetCell.contains(fid.getFiducialId())) {
                sumTx += fid.getTargetXDegrees();
                count++;
            }
        }
        return (count > 0) ? (sumTx / count) : Double.NaN;
    }

    /**
     * Returns the average vertical angle (tY) to the chosen HiveCell, or Double.NaN if not seen.
     */
    public double getCellTY(HiveCell targetCell) {
        if (!hasValidTarget()) return Double.NaN;

        List<LLResultTypes.FiducialResult> fiducials = latestResult.getFiducialResults();
        double sumTy = 0.0;
        int count = 0;

        for (LLResultTypes.FiducialResult fid : fiducials) {
            if (targetCell.contains(fid.getFiducialId())) {
                sumTy += fid.getTargetYDegrees();
                count++;
            }
        }
        return (count > 0) ? (sumTy / count) : Double.NaN;
    }

    /**
     * Calculates horizontal distance (inches) to the target HiveCell.
     * Returns -1 if target is not visible.
     */
    public double getDistanceToCell(HiveCell targetCell) {
        double ty = getCellTY(targetCell);
        if (Double.isNaN(ty)) return -1.0;

        double totalAngleRad = Math.toRadians(CAM_DEG + ty);
        return (TARGET_H - CAM_H) / Math.tan(totalAngleRad);
    }

    public void switchPipeline(int index) {
        limelight.pipelineSwitch(index);
    }

    public void stop() {
        limelight.stop();
        limelight.shutdown();
    }
}
