package sfiomn.legendarysurvivaloverhaul.config;

public enum JsonFileName
{
	ITEM("itemTemperatures.json"),
	BLOCK("blockTemperatures.json"),
	BIOME("biomeOverrides.json"),
	CONSUMABLE_TEMP("temperatureConsumables.json"),
	FUEL("fuelItems.json"),
	CONSUMABLE_THIRST("thirstConsumables.json");
	
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
