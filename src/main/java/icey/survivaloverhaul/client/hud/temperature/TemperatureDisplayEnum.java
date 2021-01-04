package icey.survivaloverhaul.client.hud.temperature;

public enum TemperatureDisplayEnum
{
	SYMBOL("symbol"),
	NONE("none");
	
	private String displayType;
	
	private TemperatureDisplayEnum(String displayType)
	{
		this.displayType = displayType;
	}
	
	public String getDisplayType()
	{
		return displayType;
	}
}
