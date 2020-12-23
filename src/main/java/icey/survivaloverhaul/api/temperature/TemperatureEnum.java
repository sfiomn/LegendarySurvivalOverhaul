package icey.survivaloverhaul.api.temperature;

// Stolen shamelessly from Charles445's SimpleDifficulty mod
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/temperature/TemperatureEnum.java
public enum TemperatureEnum
{
	HYPOTHERMIA(0, 15), // You start dying.
	FREEZING(16, 30), // You will start to recieve slowness.
	COLD(31, 40),
	NORMAL(41, 60),
	HOT(61,70), // The player will begin to sweat. This grows in intensity as the player gets hotter.
	OVERHEAT(71, 85), // You will start to randomly get the thirst effect
	HYPERTHERMIA(86,100); // You start dying.
	
	private int lowerBound;
	private int upperBound;
	
	private TemperatureEnum(int lowerBound, int upperBound)
	{
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public boolean matches(int temperature)
	{
		return (temperature>=this.lowerBound && temperature<=this.upperBound);
	}
	
	public int getMiddle()
	{
		return (this.upperBound + this.lowerBound)/2;
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
