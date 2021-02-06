package icey.survivaloverhaul.data.client;

import java.util.ArrayList;
import java.util.List;

import icey.survivaloverhaul.Main;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider
{
	public List<String> modItemList = new ArrayList<String>();
	
	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Main.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
		
		// builder(itemGenerated, "example_item");
		//builder(itemGenerated, "canteen", "canteen_empty");
		//builder(itemGenerated, "netherite_canteen", "netherite_canteen_empty");
		
		builder(itemGenerated, "infernal_fern_leaf");
		builder(itemGenerated, "stone_fern_leaf");
		
		builder(itemGenerated, "mercury_paste");
		
		builder(itemGenerated, "snow_helmet", "snow_hat");
		builder(itemGenerated, "snow_chestplate", "snow_tunic");
		builder(itemGenerated, "snow_leggings", "snow_pants");
		builder(itemGenerated, "snow_boots", "snow_boots");
		
		builder(itemGenerated, "desert_helmet", "desert_hat");
		builder(itemGenerated, "desert_chestplate", "desert_shirt");
		builder(itemGenerated, "desert_leggings", "desert_tunic");
		builder(itemGenerated, "desert_boots", "desert_sandals");
		
		builder(itemGenerated, "heart_fruit", "heart_fruit");
		/*
		for (int i = 0; i < 30; i++)
		{
			String str = "";
			if (i < 10)
			{
				str += "0";
			}
			
			str += i;
			
			builder(itemGenerated, "thermometer/thermometer_" + str);
		}
		*/
	}
	
	private ItemModelBuilder builder(ModelFile itemGenerated, String name)
	{
		return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
	}
	
	private ItemModelBuilder builder(ModelFile itemGenerated, String name, String textureName)
	{
		return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + textureName);
	}

}
