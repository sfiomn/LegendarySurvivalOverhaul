package icey.survivaloverhaul.api.temperature;

// Stolen shamelessly from Charles445's SimpleDifficulty mod
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/temperature/TemperatureEnum.java
public enum TemperatureStateEnum
{
	HYPOTHERMIA(0, 10), // You start dying.
	FREEZING(11, 20), // You will start to recieve slowness.
	COLD(21, 30),
	NORMAL(31, 40),
	HOT(41,50), // The player will begin to sweat. This grows in intensity as the player gets hotter.
	OVERHEAT(51, 60), // You will start to randomly get the thirst effect
	HYPERTHERMIA(61,70); // You start dying.
	
	private int lowerBound;
	private int upperBound;
	
	private TemperatureStateEnum(int lowerBound, int upperBound)
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
