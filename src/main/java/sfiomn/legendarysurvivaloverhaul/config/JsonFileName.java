package sfiomn.legendarysurvivaloverhaul.config;

public enum JsonFileName
{
	DIMENSION_TEMP("dimensionTemperatures.json"),
	ITEM_TEMP("itemTemperatures.json"),
	BLOCK_TEMP("blockTemperatures.json"),
	ENTITY_TEMP("entityTemperatures.json"),
	BIOME_TEMP("biomeOverrides.json"),
	CONSUMABLE_TEMP("temperatureConsumables.json"),
	FUEL("fuelItems.json"),
	BLOCK_THIRST("blockThirst.json"),
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
