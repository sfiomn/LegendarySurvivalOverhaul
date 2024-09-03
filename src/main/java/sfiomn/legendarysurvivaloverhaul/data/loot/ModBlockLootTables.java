package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.common.blocks.IceFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.SunFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.WaterPlantBlock;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.Set;

import static java.lang.System.setProperties;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockRegistry.HEATER.get());
        this.dropOther(BlockRegistry.HEATER_TOP.get(), BlockRegistry.HEATER.get());
        this.dropSelf(BlockRegistry.COOLER.get());
        this.dropSelf(BlockRegistry.ICE_FERN_CROP.get());
        this.dropSelf(BlockRegistry.SUN_FERN_CROP.get());
        this.dropSelf(BlockRegistry.SEWING_TABLE.get());

        LootItemCondition.Builder lootitemcondition$builder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(BlockRegistry.WATER_PLANT_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(WaterPlantBlock.AGE, WaterPlantBlock.MAX_AGE));

        this.add(BlockRegistry.WATER_PLANT_CROP.get(), createCropDrops(BlockRegistry.WATER_PLANT_CROP.get(), ItemRegistry.WATER_PLANT_SEEDS.get(),
                ItemRegistry.WATER_PLANT_SEEDS.get(), lootitemcondition$builder));

        lootitemcondition$builder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(BlockRegistry.ICE_FERN_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(IceFernBlock.AGE, IceFernBlock.MAX_AGE));

        this.add(BlockRegistry.ICE_FERN_CROP.get(), createCropDrops(BlockRegistry.ICE_FERN_CROP.get(), ItemRegistry.ICE_FERN_SEEDS.get(),
                ItemRegistry.ICE_FERN_SEEDS.get(), lootitemcondition$builder));

        lootitemcondition$builder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(BlockRegistry.SUN_FERN_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SunFernBlock.AGE, SunFernBlock.MAX_AGE));

        this.add(BlockRegistry.SUN_FERN_CROP.get(), createCropDrops(BlockRegistry.SUN_FERN_CROP.get(), ItemRegistry.SUN_FERN_SEEDS.get(),
                ItemRegistry.SUN_FERN_SEEDS.get(), lootitemcondition$builder));
    }

    protected LootTable.Builder createSimpleDropCount(Item item, float min, float max) {
        return createSingleItemTable(item, UniformGenerator.between(min, max));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
    }
}
