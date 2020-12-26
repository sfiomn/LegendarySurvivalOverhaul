package icey.survivaloverhaul.data.client;

import icey.survivaloverhaul.Main;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider
{
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, Main.MOD_ID, exFileHelper);
	}
		
    @Override
    protected void registerStatesAndModels() {
    	// simpleBlock(BlockRegistry.EXAMPLE_BLOCK.get());
    	
    }
}
