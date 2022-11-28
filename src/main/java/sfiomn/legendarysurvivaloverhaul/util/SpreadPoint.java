package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.ArrayList;

import static sfiomn.legendarysurvivaloverhaul.util.MathUtil.addToAverage;

public class SpreadPoint {
    private final BlockPos pos;
    private final Direction originalDirection;
    private final double spreadCapacity;
    private final double influenceDistance;
    private final World world;
    private boolean canSeeSky;
    private final ArrayList<SpreadPoint> parents;
    private final double tempInfluenceOutsideDistMultiplier = Config.Baked.tempInfluenceOutsideDistMultiplier;
    private final double tempInfluenceUpDistMultiplier = Config.Baked.tempInfluenceUpDistMultiplier;

    // TODO : remove perf logs when done with updating mod
    public static float averageProcessTimeEmptyBlockCheck = 0;
    public static int numberEmptyBlockCheck = 0;
    public static float averageProcessTimeNoCollideBlockCheck = 0;
    public static int numberNoCollideBlockCheck = 0;
    public static float averageProcessTimeCanSeeSkyCheck = 0;
    public static int numberCanSeeSkyCheck = 0;

    public SpreadPoint(BlockPos pos, Direction originalDirection, double spreadCapacity, double influenceDistance, ArrayList<SpreadPoint> parents, World world) {
        this.pos = pos;
        this.originalDirection = originalDirection;
        this.spreadCapacity = spreadCapacity;
        this.influenceDistance = influenceDistance;
        this.parents = parents;
        this.world = world;
        this.canSeeSky = false;
    }

    public double influenceDistance() {
        return influenceDistance;
    }

    public BlockPos position() {
        return pos;
    }

    public Direction originalDirection() {
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
    }

    public BlockPos newSpreadPos(Direction direction) {
        return new BlockPos(pos.getX() + direction.getStepX(), pos.getY() + direction.getStepY(), pos.getZ() + direction.getStepZ());
    }

    public void addParent(SpreadPoint newSpread) {
        parents.add(0, newSpread);
        int MAX_PARENT_SIZE = 2;
        while (parents.size() > MAX_PARENT_SIZE) {
            parents.remove(parents.size() - 1);
        }
    }

    private SpreadPoint getBestParent(Direction direction) {
        if (parents.size() > 1) {

            if (parents.get(1).distanceSq(newSpreadPos(direction)) <= 3) {
                return parents.get(1);
            }
        }
        if (parents.size() > 0) {

            if (parents.get(0).distanceSq(newSpreadPos(direction)) <= 2) {
                return parents.get(0);
            }
        }
        return this;
    }

    public SpreadPoint spreadTo(Direction direction) {
        BlockPos newBlockPos = newSpreadPos(direction);
        SpreadPoint spreadPointParent = getBestParent(direction);
        double calculatedDistance = spreadPointParent.calculateDistance(newBlockPos);
        SpreadPoint newSpreadPoint = new SpreadPoint(newBlockPos, direction, spreadPointParent.spreadCapacity - (calculatedDistance * consumptionMultiplier(spreadPointParent, newBlockPos)), spreadPointParent.influenceDistance + calculatedDistance, new ArrayList<>(this.parents), world);
        newSpreadPoint.addParent(this);
        return newSpreadPoint;
    }

    public boolean isValidSpreadPoint() {
        //  Check we can spread the temperature influence in the new position meaning either AIR or a block a player can pass through and is not liquid
        if (spreadCapacity <= 0) {
            return false;
        } else {

            //  Performance logging purpose
            // TODO : remove perf logs when done with updating mod
            if (false) {
                long start1 = System.nanoTime();
                boolean isAir = world.isEmptyBlock(pos);
                long end1 = System.nanoTime();
                SpreadPoint.averageProcessTimeEmptyBlockCheck += addToAverage(SpreadPoint.averageProcessTimeEmptyBlockCheck, SpreadPoint.numberEmptyBlockCheck, end1 - start1);
                SpreadPoint.numberEmptyBlockCheck++;

                long start = System.nanoTime();
                boolean isNoCollideCheck = (!world.getBlockState(pos).getMaterial().blocksMotion() && !world.getBlockState(pos).getMaterial().isLiquid());
                long end = System.nanoTime();
                SpreadPoint.averageProcessTimeNoCollideBlockCheck += addToAverage(SpreadPoint.averageProcessTimeNoCollideBlockCheck, SpreadPoint.numberNoCollideBlockCheck, end - start);
                SpreadPoint.numberNoCollideBlockCheck++;
            }

            return (!world.getBlockState(pos).getMaterial().blocksMotion() && !world.getBlockState(pos).getMaterial().isLiquid());
        }
    }

    public void setCanSeeSky() {
        // TODO : remove perf logs when done with updating mod
        if (false) {
            long start = System.nanoTime();
            boolean canSeeSkyCheck = world.canSeeSky(pos);
            long end = System.nanoTime();
            SpreadPoint.averageProcessTimeCanSeeSkyCheck += addToAverage(SpreadPoint.averageProcessTimeCanSeeSkyCheck, SpreadPoint.numberCanSeeSkyCheck, end - start);
            SpreadPoint.numberCanSeeSkyCheck++;
        }
        this.canSeeSky = world.canSeeSky(pos);
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

    // TODO : remove perf logs when done with updating mod
    public static void resetCounters() {
        SpreadPoint.averageProcessTimeCanSeeSkyCheck = 0;
        SpreadPoint.numberCanSeeSkyCheck = 0;
        SpreadPoint.averageProcessTimeNoCollideBlockCheck = 0;
        SpreadPoint.numberNoCollideBlockCheck = 0;
        SpreadPoint.averageProcessTimeEmptyBlockCheck = 0;
        SpreadPoint.numberEmptyBlockCheck = 0;
    }

}
