package sfiomn.legendarysurvivaloverhaul.api.temperature;

// Stolen shamelessly from Charles445's SimpleDifficulty mod
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/temperature/TemperatureEnum.java
public enum TemperatureEnum
{
	FROSTBITE(0, 7), // You start dying.
	COLD(7, 13),
	NORMAL(13, 29),
	HOT(29,35), // The player will begin to sweat. This grows in intensity as the player gets hotter.
	HEAT_STROKE(35,40); // You start dying.
	
	private final int lowerBound;
	private final int upperBound;
	
	TemperatureEnum(int lowerBound, int upperBound)
	{
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public boolean matches(float temperature)
	{
		temperature = TemperatureUtil.clampTemperature(temperature);
		return (temperature >= this.lowerBound && temperature < this.upperBound);
	}

	public static TemperatureEnum get(float temperature) {
		if (temperature < FROSTBITE.upperBound)
			return FROSTBITE;
		else if (temperature >= COLD.lowerBound && temperature < COLD.upperBound)
			return COLD;
		else if (temperature >= NORMAL.lowerBound && temperature < NORMAL.upperBound)
			return NORMAL;
		else if (temperature >= HOT.lowerBound && temperature < HOT.upperBound)
			return HOT;
		else
			return HEAT_STROKE;
	}
	
	public float getMiddle()
	{
		return (this.upperBound + this.lowerBound) / 2.0f;
	}
	
	public int getLowerBound() 
	{
		return this.lowerBound;
	}
	
	public int getUpperBound() 
	{
		return this.upperBound;
	}
}
