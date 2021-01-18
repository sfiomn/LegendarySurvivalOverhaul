package icey.survivaloverhaul.client.hud;

public enum StaminaDisplayEnum
{
	ABOVE_ARMOR("above_armor"),
	BAR("bar"),
	NONE("none");
	
	private String displayType;
	
	private StaminaDisplayEnum(String displayType)
	{
		this.displayType = displayType;
	}
	
	public String getDisplayType()
	{
		return displayType;
	}
	
	public static StaminaDisplayEnum getDisplayFromString(String str)
	{
		for(StaminaDisplayEnum sde : StaminaDisplayEnum.values())
		{
			if (sde.displayType.equalsIgnoreCase(str))
			{
				return sde;
			}
		}
		
		return NONE;
	}
}
