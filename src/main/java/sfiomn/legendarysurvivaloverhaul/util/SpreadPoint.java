package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class SpreadPoint {
    private final BlockPos pos;
    private final Direction originDirection;
    private final double spreadCapacity;
    private final double influenceDistance;
    private final World world;
    private final BlockState blockState;
    private boolean canSeeSky;
    private boolean isWater;
    private final double tempInfluenceOutsideDistMultiplier = Config.Baked.tempInfluenceOutsideDistMultiplier;
    private final double tempInfluenceUpDistMultiplier = Config.Baked.tempInfluenceUpDistMultiplier;
    private final double tempInfluenceInWaterDistMultiplier = Config.Baked.tempInfluenceInWaterDistMultiplier;

    public SpreadPoint(BlockPos pos, Direction originDirection, double spreadCapacity, double influenceDistance, World world) {
        this.pos = pos;
        this.originDirection = originDirection;
        this.spreadCapacity = spreadCapacity;
        this.influenceDistance = influenceDistance;
        this.world = world;
        this.blockState = world.getBlockState(pos);
        this.canSeeSky = false;
        this.isWater = false;
    }

    public double influenceDistance() {
        return influenceDistance;
    }

    public BlockPos position() {
        return pos;
    }

    public Direction originDirection() {
        return originDirection;
    }

    public double spreadCapacity() {
        return spreadCapacity;
    }

    public SpreadPoint spreadTo(BlockPos newBlockPos, Direction originDirection, float distance) {
        return new SpreadPoint(newBlockPos, originDirection, this.spreadCapacity - (distance * consumptionMultiplier(originDirection)), this.influenceDistance + distance, world);
    }

    public boolean isValidSpreadPoint(Direction originDirection) {
        //  Check we can spread the temperature influence in the new position meaning either AIR or a block a player can pass through and is not liquid
        if (spreadCapacity <= 0) {
            return false;
        } else {
            if (blockState.isAir())
                return true;
            if (blockState.is(Blocks.WATER)) {
                this.isWater = true;
                return true;
            }

            return !blockState.isFaceSturdy(world, pos, originDirection.getOpposite());
        }
    }

    public boolean isValidSpreadDirection(Direction direction) {
        return !blockState.isFaceSturdy(world, pos, direction);
    }

    public void setCanSeeSky() {
        this.canSeeSky = world.dimensionType().hasCeiling() || world.canSeeSky(pos);
    }

    private double consumptionMultiplier(Direction originDirection) {
        boolean upDirection = originDirection.getNormal().getY() > 0;
        double consumptionMultiplier = 1;

        if (this.isWater) {
            consumptionMultiplier *= tempInfluenceInWaterDistMultiplier;
        } else if (this.canSeeSky) {
            consumptionMultiplier *= tempInfluenceOutsideDistMultiplier;
        }
        if (upDirection) {
            consumptionMultiplier *= tempInfluenceUpDistMultiplier;
        }
        return (1 / consumptionMultiplier);
    }
}
