package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.tileentity.CoolerTileEntity;
import sfiomn.legendarysurvivaloverhaul.common.tileentity.HeaterTileEntity;
import sfiomn.legendarysurvivaloverhaul.common.tileentity.SewingTableTileEntity;

public class TileEntityRegistry {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, LegendarySurvivalOverhaul.MOD_ID);

    public static RegistryObject<TileEntityType<HeaterTileEntity>> HEATER_TILE_ENTITY =
            TILE_ENTITIES.register("heater_tile_entity", () -> TileEntityType.Builder.of(
                    HeaterTileEntity::new, BlockRegistry.HEATER.get()).build(null));

    public static RegistryObject<TileEntityType<CoolerTileEntity>> COOLER_TILE_ENTITY =
            TILE_ENTITIES.register("cooler_tile_entity", () -> TileEntityType.Builder.of(
                    CoolerTileEntity::new, BlockRegistry.COOLER.get()).build(null));

    public static RegistryObject<TileEntityType<SewingTableTileEntity>> SEWING_TABLE_TILE_ENTITY =
            TILE_ENTITIES.register("sewing_table_tile_entity", () -> TileEntityType.Builder.of(
                    SewingTableTileEntity::new, BlockRegistry.SEWING_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
