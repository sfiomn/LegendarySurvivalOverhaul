package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.AbstractThermalContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.CoolerContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.HeaterContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<MenuType<AbstractThermalContainer>> COOLER_CONTAINER
            = CONTAINERS.register("cooler_container", () -> IForgeMenuType.create(CoolerContainer::new));

    public static final RegistryObject<MenuType<AbstractThermalContainer>> HEATER_CONTAINER
            = CONTAINERS.register("heater_container", () -> IForgeMenuType.create(HeaterContainer::new));

    public static final RegistryObject<MenuType<SewingTableContainer>> SEWING_TABLE_CONTAINER
            = CONTAINERS.register("sewing_table_container", () -> IForgeMenuType.create(SewingTableContainer::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
