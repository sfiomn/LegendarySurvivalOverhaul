package icey.survivaloverhaul.util;

public final class MathUtil
{
	private MathUtil() {}
	
	public static float invLerp(float from, float to, float value)
	{
		return (value - from) / (to - from);
	}
}
