package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.CoolerContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.HeaterContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.AbstractThermalContainer;

public class ContainerRegistry {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<ContainerType<AbstractThermalContainer>> COOLER_CONTAINER
            = CONTAINERS.register("cooler_container", () -> IForgeContainerType.create(CoolerContainer::new));

    public static final RegistryObject<ContainerType<AbstractThermalContainer>> HEATER_CONTAINER
            = CONTAINERS.register("heater_container", () -> IForgeContainerType.create(HeaterContainer::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
