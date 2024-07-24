package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

import java.util.Random;

public class HeaterBaseBlock extends ThermalBlock {

    public static final Properties properties = getProperties();

    private static final VoxelShape FEET = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 6.0d, 16.0d);
    private static final VoxelShape BASE = Block.box(1.0d, 6.0d, 1.0d, 15.0d, 16.0d, 15.0d);

    private static final VoxelShape XZ_AXIS_AABB = VoxelShapes.or(FEET, BASE);

    public HeaterBaseBlock(ThermalTypeEnum thermalType) {
        super(thermalType, properties);
    }

    public static Properties getProperties()
    {
        return Properties
                .of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(3f, 10f)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .noOcclusion()
                .lightLevel((lightLevel) -> lightLevel.getValue(BlockStateProperties.LIT) ? 13 : 0);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return XZ_AXIS_AABB;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {

        World level = context.getLevel();
        BlockPos topPos = context.getClickedPos().above();
        return level.getBlockState(topPos).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(topPos)
                ? this.defaultBlockState().setValue(FACING, context.getHorizontalDirection())
                : null;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        super.onRemove(state, world, pos, newState, isMoving);
        if(!state.is(newState.getBlock()) && world.getBlockState(pos.above()).getBlock() instanceof HeaterTopBlock) {
            world.removeBlock(pos.above(), false);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (world.isEmptyBlock(pos.above()))
        {
            world.setBlock(pos.above(), BlockRegistry.HEATER_TOP.get().defaultBlockState().setValue(HeaterTopBlock.FACING, state.getValue(FACING)), 2);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        if (state.getValue(LIT)) {
            float chance = 0.5f;
            float chance_flame = 0.4f;

            //  Middle of the block
            double posX = pos.getX();
            double posY = pos.getY();
            double posZ = pos.getZ();

            if (rand.nextFloat() < chance) {
                worldIn.playLocalSound(posX + 0.5d, posY + 0.5d, posZ + 0.5d, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            if (rand.nextFloat() < chance) {
                //  Particle spawns half block around the center of the block : [0.25 - 0.75]
                float xr = rand.nextFloat() / 2 + 0.25f;
                float zr = rand.nextFloat() / 2 + 0.25f;

                //  Particle moves 1/20 block any direction : rand range [0.0 - 0.05] -> [-0.025 - 0.025]
                float xm = rand.nextFloat() / 20 - 0.025f;
                float zm = rand.nextFloat() / 20 - 0.025f;

                //  Particles moves always up : [0.02 - 0.03]
                float ym = rand.nextFloat() / 10 + 0.02f;

                worldIn.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, posX + xr, pos.above().getY() + 0.8d, posZ + zr, xm, ym, zm);
            }

            if (rand.nextFloat() < chance_flame) {
                //  Particle spawns around the center of the block : [0.33 - 0.66]
                float zr = rand.nextFloat() / 3 + 0.33f;
                float yr = rand.nextFloat() / 6 + 0.5f;
                worldIn.addParticle(ParticleTypes.SMOKE, false, posX + 0.05, posY + yr, posZ + zr, 0, 0, 0);
                worldIn.addParticle(ParticleTypes.FLAME, false, posX + 0.05, posY + yr, posZ + zr, 0, 0, 0);
            }
            if (rand.nextFloat() < chance_flame) {
                //  Particle spawns around the center of the block : [0.33 - 0.66]
                float zr = rand.nextFloat() / 3 + 0.33f;
                float yr = rand.nextFloat() / 6 + 0.5f;
                worldIn.addParticle(ParticleTypes.SMOKE, false, posX + 0.95, posY + yr, posZ + zr, 0, 0, 0);
                worldIn.addParticle(ParticleTypes.FLAME, false, posX + 0.95, posY + yr, posZ + zr, 0, 0, 0);
            }
            if (rand.nextFloat() < chance_flame) {
                //  Particle spawns around the center of the block : [0.33 - 0.66]
                float xr = rand.nextFloat() / 3 + 0.33f;
                float yr = rand.nextFloat() / 6 + 0.5f;
                worldIn.addParticle(ParticleTypes.SMOKE, false, posX + xr, posY + yr, posZ + 0.05, 0, 0, 0);
                worldIn.addParticle(ParticleTypes.FLAME, false, posX + xr, posY + yr, posZ + 0.05, 0, 0, 0);
            }
            if (rand.nextFloat() < chance_flame) {
                //  Particle spawns around the center of the block : [0.33 - 0.66]
                float xr = rand.nextFloat() / 3 + 0.33f;
                float yr = rand.nextFloat() / 6 + 0.5f;
                worldIn.addParticle(ParticleTypes.SMOKE, false, posX + xr, posY + yr, posZ + 0.95, 0, 0, 0);
                worldIn.addParticle(ParticleTypes.FLAME, false, posX + xr, posY + yr, posZ + 0.95, 0, 0, 0);
            }
        }
        super.animateTick(state, worldIn, pos, rand);
    }
}
