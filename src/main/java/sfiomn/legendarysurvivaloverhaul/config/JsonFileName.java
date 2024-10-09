package sfiomn.legendarysurvivaloverhaul.config;

public enum JsonFileName
{
	DIMENSION_TEMP("dimensionTemperatures.json"),
	BIOME_TEMP("biomeOverrides.json"),
	ITEM_TEMP("itemTemperatures.json"),
	ENTITY_TEMP("entityTemperatures.json"),
	BLOCK_TEMP("blockTemperatures.json"),
	ORIGINS_TEMP("originsTemperatures.json"),
	FUEL("fuelItems.json"),
	CONSUMABLE_TEMP("temperatureConsumables.json"),
	BLOCK_THIRST("blockThirst.json"),
	CONSUMABLE_THIRST("thirstConsumables.json"),
	CONSUMABLE_HEAL("healingConsumables.json"),
	DAMAGE_SOURCE_BODY_PARTS("damageSourceBodyParts.json");
	
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
