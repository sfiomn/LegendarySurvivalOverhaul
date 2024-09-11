package sfiomn.legendarysurvivaloverhaul.config;

public enum JsonFileName
{
	DIMENSION("dimensionTemperatures.json"),
	BIOME("biomeOverrides.json"),
	ITEM("itemTemperatures.json"),
	ENTITY("entityTemperatures.json"),
	BLOCK("blockTemperatures.json"),
	FUEL("fuelItems.json"),
	CONSUMABLE_TEMP("temperatureConsumables.json"),
	CONSUMABLE_THIRST("thirstConsumables.json"),
	CONSUMABLE_HEAL("healingConsumables.json"),
	DAMAGE_SOURCE_BODY_PARTS("damageSourceBodyParts.json");;
	
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
