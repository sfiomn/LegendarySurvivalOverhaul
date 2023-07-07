package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

import java.util.Random;

public class CoolerBlock extends ThermalBlock {

    public static final Properties properties = getProperties();

    public CoolerBlock(ThermalTypeEnum thermalType) {
        super(thermalType, properties);
    }

    public static Properties getProperties()
    {
        return Properties
                .of(Material.WOOD)
                .sound(SoundType.WOOD)
                .strength(2f, 10f)
                .harvestTool(ToolType.AXE)
                .harvestLevel(1)
                .noOcclusion();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        if (state.getValue(LIT)) {
            float chanceSound = 0.1f;
            float chanceParticle = 0.25f;
            double posX = pos.getX();
            double posY = pos.getY();
            double posZ = pos.getZ();
            if (rand.nextFloat() < chanceSound) {
                worldIn.playLocalSound(posX + 0.5d, posY + 0.5d, posZ + 0.5d, SoundRegistry.COOLER_BLOCK.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            if (rand.nextFloat() < chanceParticle) {

                //  Spawn particles at the top of the cooler, randomly along the surface
                float xr = rand.nextFloat();
                float zr = rand.nextFloat();

                worldIn.addParticle(ParticleTypes.ITEM_SNOWBALL, false, posX + xr, posY + 1.0d, posZ + zr, 0, 0, 0);
            }
        }
        super.animateTick(state, worldIn, pos, rand);
    }
}
