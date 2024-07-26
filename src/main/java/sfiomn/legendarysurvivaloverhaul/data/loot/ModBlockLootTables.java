package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockRegistry.HEATER.get());
        this.dropOther(BlockRegistry.HEATER_TOP.get(), BlockRegistry.HEATER.get());
        this.dropSelf(BlockRegistry.COOLER.get());
        this.dropSelf(BlockRegistry.ICE_FERN.get());
        this.dropSelf(BlockRegistry.SUN_FERN.get());
        this.dropSelf(BlockRegistry.SEWING_TABLE.get());

        this.add(BlockRegistry.WATER_PLANT.get(),
                block -> createSimpleDropCount(BlockRegistry.WATER_PLANT.get(), ItemRegistry.WATER_PLANT_BAG.get(), 2.0f, 4.0f));
    }

    protected LootTable.Builder createSimpleDropCount(Block block, Item item, float min, float max) {
        return createSilkTouchDispatchTable(block,
                this.applyExplosionDecay(block,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
