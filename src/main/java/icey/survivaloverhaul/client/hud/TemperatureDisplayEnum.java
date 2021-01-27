package icey.survivaloverhaul.client.hud;

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
	
	public static TemperatureDisplayEnum getDisplayFromString(String str)
	{
		for(TemperatureDisplayEnum tde : TemperatureDisplayEnum.values())
		{
			if (tde.displayType.equalsIgnoreCase(str))
				return tde;
		}
		
		return NONE;
	}
}
