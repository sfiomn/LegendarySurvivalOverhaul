package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.function.BiConsumer;

public class ModLootTables implements LootTableSubProvider {
    public ModLootTables() {
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        biConsumer.accept(
                new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "inject/heart_fruits"),
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(ItemRegistry.HEART_FRUIT.get()).setWeight(15))
                                .add(LootItem.lootTableItem(ItemRegistry.HEART_FRUIT.get()).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0f))))
                                .add(EmptyLootItem.emptyItem().setWeight(80))
                ));
    }
}
