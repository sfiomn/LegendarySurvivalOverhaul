package sfiomn.legendarysurvivaloverhaul.util;

public final class MathUtil
{
	private MathUtil() {}
	
	public static float invLerp(float from, float to, float value)
	{
		return (value - from) / (to - from);
	}

	public static float round (float value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (float) Math.round(value * scale) / scale;
	}
}
