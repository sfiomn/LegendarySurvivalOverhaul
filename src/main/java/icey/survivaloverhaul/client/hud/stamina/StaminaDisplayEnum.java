package icey.survivaloverhaul.client.hud.stamina;

public enum StaminaDisplayEnum
{
	ABOVE_HOTBAR("above_hotbar"),
	WHEEL("wheel"),
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
}
