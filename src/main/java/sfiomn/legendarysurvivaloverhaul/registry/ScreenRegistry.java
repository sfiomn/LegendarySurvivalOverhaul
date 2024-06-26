package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.client.screens.ThermalScreen;
import sfiomn.legendarysurvivaloverhaul.common.containers.CoolerContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.HeaterContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;

public class ScreenRegistry {
    public static final DeferredRegister<MenuType<?>> SCREEN = DeferredRegister.create(ForgeRegistries.MENU_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<MenuType<CoolerContainer>> COOLER_SCREEN = SCREEN.register("cooler", () -> IForgeMenuType.create(CoolerContainer::new));
    public static final RegistryObject<MenuType<HeaterContainer>> HEATER_SCREEN = SCREEN.register("heater", () -> IForgeMenuType.create(HeaterContainer::new));
    public static final RegistryObject<MenuType<SewingTableContainer>> SEWING_TABLE_SCREEN = SCREEN.register("sewing_table", () -> IForgeMenuType.create(SewingTableContainer::new));
}
