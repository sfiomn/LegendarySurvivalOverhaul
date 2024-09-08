package sfiomn.legendarysurvivaloverhaul.common.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class DoubleBlockFeature extends Feature<SimpleBlockConfiguration> {
    public DoubleBlockFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        SimpleBlockConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        BlockState state = config.toPlace().getState(context.random(), pos);
        if (state.canSurvive(level, pos)) {
            if (!level.isEmptyBlock(pos.above())) {
                return false;
            }

            DoublePlantBlock.placeAt(level, state, pos, 2);

            return true;
        } else {
            return false;
        }
    }
}
