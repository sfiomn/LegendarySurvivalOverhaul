package sfiomn.legendarysurvivaloverhaul.common.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.DefaultFlowersFeature;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.blocks.IceFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.SunFernBlock;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

import java.util.Random;

public class FernFeature extends DefaultFlowersFeature {
    public FernFeature(Codec<BlockClusterFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        BlockState blockstate = config.stateProvider.getState(rand, pos);
        if (blockstate.is(BlockRegistry.ICE_FERN_CROP.get()))
            blockstate = blockstate.setValue(IceFernBlock.AGE, IceFernBlock.MAX_AGE);
        else if (blockstate.is(BlockRegistry.SUN_FERN_CROP.get())) {
            blockstate = blockstate.setValue(SunFernBlock.AGE, SunFernBlock.MAX_AGE);
        }

        int i = 0;
        for (int j = 0; j < this.getCount(config); ++j) {
            BlockPos blockpos = this.getPos(rand, pos, config);
            blockpos = worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, blockpos);
            if ((worldIn.isEmptyBlock(blockpos) || config.canReplace && worldIn.getBlockState(blockpos).getMaterial().isReplaceable()) && blockpos.getY() < worldIn.getMaxBuildHeight() - 1 && blockstate.canSurvive(worldIn, blockpos) && this.isValid(worldIn, blockpos, config)) {
                worldIn.setBlock(blockpos, blockstate, 2);
                ++i;
            }
        }
        return i > 0;
    }
}
