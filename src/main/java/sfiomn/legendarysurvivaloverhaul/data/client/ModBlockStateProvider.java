package sfiomn.legendarysurvivaloverhaul.data.client;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider
{
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, LegendarySurvivalOverhaul.MOD_ID, exFileHelper);
	}
		
    @Override
    protected void registerStatesAndModels() {
    	// simpleBlock(BlockRegistry.EXAMPLE_BLOCK.get());
    	
    }
}
