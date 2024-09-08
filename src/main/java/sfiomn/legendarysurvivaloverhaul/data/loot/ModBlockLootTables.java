package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
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
        this.dropSelf(BlockRegistry.SEWING_TABLE.get());

        LootItemCondition.Builder lootitemconditionMaxAgebuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(BlockRegistry.WATER_PLANT_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(WaterPlantBlock.AGE, WaterPlantBlock.MAX_AGE));

        LootItemCondition.Builder lootitemconditionBelow1MaxAgebuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(BlockRegistry.WATER_PLANT_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(WaterPlantBlock.AGE, WaterPlantBlock.MAX_AGE - 1));

        LootItemCondition.Builder lootitemconditionBelow2MaxAgebuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(BlockRegistry.WATER_PLANT_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(WaterPlantBlock.AGE, WaterPlantBlock.MAX_AGE - 2));

        this.add(BlockRegistry.WATER_PLANT_CROP.get(),
                createCropDrops(
                        BlockRegistry.WATER_PLANT_CROP.get(),
                        ItemRegistry.WATER_PLANT_SEEDS.get(),
                        ItemRegistry.WATER_PLANT_SEEDS.get(),
                        lootitemconditionMaxAgebuilder
                )
                        .withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(ItemRegistry.WATER_PLANT_BAG.get()))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                                .when(lootitemconditionBelow2MaxAgebuilder)
                        )
                        .withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(ItemRegistry.WATER_PLANT_BAG.get()))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                                .when(lootitemconditionBelow1MaxAgebuilder)
                        )
                        .withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(ItemRegistry.WATER_PLANT_BAG.get()))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f)))
                                .when(lootitemconditionMaxAgebuilder)
                        )
        );

        lootitemconditionMaxAgebuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(BlockRegistry.ICE_FERN_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(IceFernBlock.AGE, IceFernBlock.MAX_AGE));

        this.add(BlockRegistry.ICE_FERN_CROP.get(),
                this.applyExplosionDecay(BlockRegistry.ICE_FERN_CROP.get(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool()
                                        .add(LootItem.lootTableItem(ItemRegistry.ICE_FERN.get()))
                                        .when(lootitemconditionMaxAgebuilder))
                                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ItemRegistry.ICE_FERN_SEEDS.get())))));

        lootitemconditionMaxAgebuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(BlockRegistry.SUN_FERN_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SunFernBlock.AGE, SunFernBlock.MAX_AGE));

        this.add(BlockRegistry.SUN_FERN_CROP.get(),
                this.applyExplosionDecay(BlockRegistry.SUN_FERN_CROP.get(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool()
                                        .add(LootItem.lootTableItem(ItemRegistry.SUN_FERN.get()))
                                        .when(lootitemconditionMaxAgebuilder))
                                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ItemRegistry.SUN_FERN_SEEDS.get())))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
    }
}
