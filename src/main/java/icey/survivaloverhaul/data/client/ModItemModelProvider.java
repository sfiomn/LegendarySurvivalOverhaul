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
		@SuppressWarnings("unused")
		ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
		
		// builder(itemGenerated, "example_item");
		builder(itemGenerated, "canteen", "canteen_empty");
	}
	
	@SuppressWarnings("unused")
	private ItemModelBuilder builder(ModelFile itemGenerated, String name)
	{
		return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
	}
	
	private ItemModelBuilder builder(ModelFile itemGenerated, String name, String textureName)
	{
		return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + textureName);
	}

}
