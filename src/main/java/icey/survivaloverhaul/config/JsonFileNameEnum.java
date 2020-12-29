package icey.survivaloverhaul.config;

public enum JsonFileNameEnum
{
	ARMOR("temperature_armor.json"),
	BLOCKS("temperature_block.json"),
	CONSUMABLE("temperature_consumable.json"),
	ITEMS("temperature_held_item.json"),
	BIOME("temperature_biome_override.json");
	
	private String fileName;
	
	private JsonFileNameEnum(String fileName)
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
