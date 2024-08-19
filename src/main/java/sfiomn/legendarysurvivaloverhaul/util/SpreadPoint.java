package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class SpreadPoint {
    private final BlockPos pos;
    private final Vec3i originalDirection;
    private final double spreadCapacity;
    private final double influenceDistance;
    private final Level world;
    private boolean canSeeSky;
    private final double tempInfluenceOutsideDistMultiplier = Config.Baked.tempInfluenceOutsideDistMultiplier;
    private final double tempInfluenceUpDistMultiplier = Config.Baked.tempInfluenceUpDistMultiplier;

    public SpreadPoint(BlockPos pos, Vec3i normalDirection, double spreadCapacity, double influenceDistance, Level world) {
        this.pos = pos;
        this.originalDirection = normalDirection;
        this.spreadCapacity = spreadCapacity;
        this.influenceDistance = influenceDistance;
        this.world = world;
        this.canSeeSky = false;
    }

    public double influenceDistance() {
        return influenceDistance;
    }

    public BlockPos position() {
        return pos;
    }

    public Vec3i originalDirection() {
        return originalDirection;
    }

    public double spreadCapacity() {
        return spreadCapacity;
    }

    public double distanceSq(BlockPos posTo) {
        double d1 = pos.getX() - posTo.getX();
        double d2 = pos.getY() - posTo.getY();
        double d3 = pos.getZ() - posTo.getZ();
        return d1 * d1 + d2 * d2 + d3 * d3;
    }

    private double calculateDistance(BlockPos posTo) {
        return Math.sqrt(distanceSq(posTo));
        //return distanceSq(posTo);
    }

    public BlockPos newSpreadPos(Vec3i normalDirection) {
        return new BlockPos(pos.getX() + normalDirection.getX(), pos.getY() + normalDirection.getY(), pos.getZ() + normalDirection.getZ());
    }

    public double newSpreadCapacity(Vec3i normalDirection) {
        BlockPos newBlockPos = newSpreadPos(normalDirection);
        //SpreadPoint spreadPointParent = getBestParent(normalDirection);
        SpreadPoint spreadPointParent = this;
        double calculatedDistance = spreadPointParent.calculateDistance(newBlockPos);

        return spreadPointParent.spreadCapacity - (calculatedDistance * consumptionMultiplier(spreadPointParent, newBlockPos));
    }

    public SpreadPoint spreadTo(Vec3i normalDirection) {
        BlockPos newBlockPos = newSpreadPos(normalDirection);
        SpreadPoint spreadPointParent = this;
        double calculatedDistance = spreadPointParent.calculateDistance(newBlockPos);

        return new SpreadPoint(newBlockPos, normalDirection, spreadPointParent.spreadCapacity - (calculatedDistance * consumptionMultiplier(spreadPointParent, newBlockPos)), spreadPointParent.influenceDistance + calculatedDistance, world);
    }

    public boolean isValidSpreadPoint() {
        //  Check we can spread the temperature influence in the new position meaning either AIR or a block a player can pass through
        if (spreadCapacity <= 0) {
            return false;
        } else {
            return world.getBlockState(pos).isAir() || !world.getBlockState(pos).blocksMotion();
        }
    }

    public void setCanSeeSky() {
        this.canSeeSky = world.dimensionType().hasCeiling() || world.canSeeSky(pos);
    }

    private double consumptionMultiplier(SpreadPoint parentSpreadPoint, BlockPos posTo) {
        double deltaY = posTo.getY() - parentSpreadPoint.position().getY();
        double consumptionMultiplier = 1;
        if (this.canSeeSky) {
            consumptionMultiplier *= tempInfluenceOutsideDistMultiplier;
        }
        if (deltaY > 0) {
            consumptionMultiplier *= tempInfluenceUpDistMultiplier;
        }
        return (1 / consumptionMultiplier);
    }
}
