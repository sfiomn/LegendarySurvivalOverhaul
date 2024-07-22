package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.blocks.CoolerBlock;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

import static sfiomn.legendarysurvivaloverhaul.common.blocks.ThermalBlock.FACING;

public class ModBlockStateProvider extends BlockStateProvider {

    public static final ResourceLocation COOLER_OFF = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/cooler_off");
    public static final ResourceLocation COOLER_ON = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/cooler_on");
    public static final ResourceLocation HEATER_BASE_OFF = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/heater_base_off");
    public static final ResourceLocation HEATER_BASE_ON = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/heater_base_on");
    public static final ResourceLocation HEATER_TOP = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/heater_top");
    public static final ResourceLocation ICE_FERN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/ice_fern");
    public static final ResourceLocation SUN_FERN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/sun_fern");
    public static final ResourceLocation SEWING_TABLE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/sewing_table");
    public static final ResourceLocation WATER_PLANT_BOTTOM = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/water_plant_bottom");
    public static final ResourceLocation WATER_PLANT_TOP = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/water_plant_top");

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, LegendarySurvivalOverhaul.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockItem(BlockRegistry.COOLER.get(), new ModelFile.UncheckedModelFile(COOLER_OFF));
        this.getVariantBuilder(BlockRegistry.COOLER.get())
                .forAllStates(state -> {
                    if (state.getValue(BlockStateProperties.LIT))
                        return ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(COOLER_ON))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build();
                    else
                        return ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(COOLER_OFF))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build();
                });

        simpleBlockItem(BlockRegistry.HEATER.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/heater_off_full_block")));
        this.getVariantBuilder(BlockRegistry.HEATER.get())
                .forAllStates(state -> {
                    if (state.getValue(BlockStateProperties.LIT))
                        return ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(HEATER_BASE_ON))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build();
                    else
                        return ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(HEATER_BASE_OFF))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build();
                });

        this.getVariantBuilder(BlockRegistry.HEATER_TOP.get())
                .forAllStates(state -> ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(HEATER_TOP))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build());

        simpleBlockItem(BlockRegistry.SEWING_TABLE.get(), new ModelFile.UncheckedModelFile(SEWING_TABLE));
        this.getVariantBuilder(BlockRegistry.SEWING_TABLE.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(new ModelFile.UncheckedModelFile(SEWING_TABLE))
                        .rotationY((int) state.getValue(FACING).toYRot())
                        .build());

        simpleBlock(BlockRegistry.ICE_FERN.get(), new ModelFile.UncheckedModelFile(ICE_FERN));
        simpleBlock(BlockRegistry.SUN_FERN.get(), new ModelFile.UncheckedModelFile(SUN_FERN));

        this.getVariantBuilder(BlockRegistry.WATER_PLANT.get())
                .partialState()
                    .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                    .modelForState()
                    .modelFile(models().cross("water_plant_bottom", WATER_PLANT_BOTTOM).texture("particle", WATER_PLANT_BOTTOM).renderType("cutout"))
                    .addModel()
                .partialState()
                    .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                    .modelForState()
                    .modelFile(models().cross("water_plant_top", WATER_PLANT_TOP).texture("particle", WATER_PLANT_TOP).renderType("cutout"))
                    .addModel();

    }
}
