package sfiomn.legendarysurvivaloverhaul.api.config.json;

public enum TemporaryModifierGroup
{
	FOOD("food"),
	DRINK("drink");
	
	private String group;
	
	private TemporaryModifierGroup(String group)
	{
		this.group = group;
	}
	
	public String group()
	{
		return group;
	}
}
