package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.util.math.AxisAlignedBB;

public final class MathUtil
{
	private MathUtil() {}

	// Return value between [0-1] based on the position of value vs the boundaries
	public static float invLerp(float from, float to, float value)
	{
		return (value - from) / (to - from);
	}

	public static float round (float value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (float) Math.round(value * scale) / scale;
	}

	public static AxisAlignedBB inflateMultiplier(AxisAlignedBB aabb1, AxisAlignedBB aabb2) {
		double newMinX = aabb1.minX + (aabb1.getXsize() * aabb2.minX);
		double newMinY = aabb1.minY + (aabb1.getYsize() * aabb2.minY);
		double newMinZ = aabb1.minZ + (aabb1.getZsize() * aabb2.minZ);
		double newMaxX = aabb1.minX + (aabb1.getXsize() * aabb2.maxX);
		double newMaxY = aabb1.minY + (aabb1.getYsize() * aabb2.maxY);
		double newMaxZ = aabb1.minZ + (aabb1.getZsize() * aabb2.maxZ);
		return new AxisAlignedBB(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
	}

	public static AxisAlignedBB inflateMultiplier(AxisAlignedBB original, double inflateValue) {
		AxisAlignedBB inflateAABB = new AxisAlignedBB(-inflateValue, -inflateValue, -inflateValue, 1 + inflateValue, 1 + inflateValue, 1 + inflateValue);
		return inflateMultiplier(original, inflateAABB);
	}

	public static AxisAlignedBB inflate(AxisAlignedBB original, AxisAlignedBB inflateValues) {
		double newMinX = original.minX + inflateValues.minX;
		double newMinY = original.minY + inflateValues.minY;
		double newMinZ = original.minZ + inflateValues.minZ;
		double newMaxX = original.maxX + inflateValues.maxX;
		double newMaxY = original.maxY + inflateValues.maxY;
		double newMaxZ = original.maxZ + inflateValues.maxZ;
		return new AxisAlignedBB(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
	}

	public static AxisAlignedBB horizontalInflate(AxisAlignedBB original, double inflateValue) {
		AxisAlignedBB inflateAABB = new AxisAlignedBB(-inflateValue, 0, -inflateValue, inflateValue, 0, inflateValue);
		return inflate(original, inflateAABB);
	}

	public static AxisAlignedBB horizontalUpInflate(AxisAlignedBB original, double inflateValue) {
		AxisAlignedBB inflateAABB = new AxisAlignedBB(-inflateValue, 0, -inflateValue, inflateValue, inflateValue, inflateValue);
		return inflate(original, inflateAABB);
	}

	public static AxisAlignedBB horizontalDownInflate(AxisAlignedBB original, double inflateValue) {
		AxisAlignedBB inflateAABB = new AxisAlignedBB(-inflateValue, -inflateValue, -inflateValue, inflateValue, 0, inflateValue);
		return inflate(original, inflateAABB);
	}
}
