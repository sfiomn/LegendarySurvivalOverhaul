package sfiomn.legendarysurvivaloverhaul.util;

public final class MathUtil
{
	private MathUtil() {}
	
	public static float invLerp(float from, float to, float value)
	{
		return (value - from) / (to - from);
	}

	public static float addToAverage(float averageValue, int nbTime, float newValueToAdd) {
		return (float) (1 / (nbTime + 1)) * (newValueToAdd - averageValue);
	}
}
