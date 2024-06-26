package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.CoolerBlockEntity;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.HeaterBlockEntity;

public class BlockEntityRegistry {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static RegistryObject<BlockEntityType<HeaterBlockEntity>> HEATER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(LegendarySurvivalOverhaul.MOD_ID + "heater_block_entity", () -> BlockEntityType.Builder
                    .of(HeaterBlockEntity::new, BlockRegistry.HEATER.get()).build(null));

    public static RegistryObject<BlockEntityType<CoolerBlockEntity>> COOLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(LegendarySurvivalOverhaul.MOD_ID + "cooler_block_entity", () -> BlockEntityType.Builder
                    .of(CoolerBlockEntity::new, BlockRegistry.COOLER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
