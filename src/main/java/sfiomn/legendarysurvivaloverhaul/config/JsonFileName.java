package sfiomn.legendarysurvivaloverhaul.config;

public enum JsonFileName
{
	ARMOR("armorTemperatures.json"),
	BLOCK("blockTemperatures.json"),
	LIQUID("liquidTemperatures.json"),
	BIOME("biomeOverrides.json"),
	CONSUMABLE("temperatureConsumables.json"),
	FUEL("fuelItems.json");
	
	private String fileName;
	
	private JsonFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	@Override
	public String toString()
	{
		return this.fileName;
	}
	
	public String get()
	{
		return this.toString();
	}
}
