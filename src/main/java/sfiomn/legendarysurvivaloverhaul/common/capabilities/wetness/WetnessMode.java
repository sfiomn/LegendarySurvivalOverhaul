package sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness;

public enum WetnessMode
{
	DISABLE("disable"),
	SIMPLE("simple"),
	DYNAMIC("dynamic");
	
	public String mode;
	
	private WetnessMode(String mode)
	{
		this.mode = mode;
	}
	
	public static WetnessMode getDisplayFromString(String str)
	{
		for(WetnessMode wm : WetnessMode.values())
		{
			if (wm.mode.equalsIgnoreCase(str))
			{
				return wm;
			}
		}
		
		return DYNAMIC;
	}
}
