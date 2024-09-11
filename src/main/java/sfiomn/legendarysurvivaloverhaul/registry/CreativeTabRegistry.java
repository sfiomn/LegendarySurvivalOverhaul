package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.List;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> ITEM_GROUPS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<CreativeModeTab> LEGENDARY_CREATURES_TAB = ITEM_GROUPS.register("legendary_creatures", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.THERMOMETER.get().getDefaultInstance())
            .displayItems((parameters, list) ->
            {
                ItemStack fullCanteen = ItemRegistry.CANTEEN.get().getDefaultInstance();
                ItemStack fullLargeCanteen = ItemRegistry.LARGE_CANTEEN.get().getDefaultInstance();
                ThirstUtil.setCapacityTag(fullCanteen, Config.Baked.canteenCapacity);
                ThirstUtil.setHydrationEnumTag(fullCanteen, HydrationEnum.NORMAL);
                ThirstUtil.setCapacityTag(fullLargeCanteen, Config.Baked.largeCanteenCapacity);
                ThirstUtil.setHydrationEnumTag(fullLargeCanteen, HydrationEnum.NORMAL);

                list.acceptAll(List.of(
                        ItemRegistry.APPLE_JUICE.get().getDefaultInstance(),
                        ItemRegistry.BEETROOT_JUICE.get().getDefaultInstance(),
                        ItemRegistry.CACTUS_JUICE.get().getDefaultInstance(),
                        ItemRegistry.CARROT_JUICE.get().getDefaultInstance(),
                        ItemRegistry.CHORUS_FRUIT_JUICE.get().getDefaultInstance(),
                        ItemRegistry.GLISTERING_MELON_JUICE.get().getDefaultInstance(),
                        ItemRegistry.GOLDEN_APPLE_JUICE.get().getDefaultInstance(),
                        ItemRegistry.GOLDEN_CARROT_JUICE.get().getDefaultInstance(),
                        ItemRegistry.MELON_JUICE.get().getDefaultInstance(),
                        ItemRegistry.PUMPKIN_JUICE.get().getDefaultInstance(),

                        ItemRegistry.CANTEEN.get().getDefaultInstance(),
                        ItemRegistry.LARGE_CANTEEN.get().getDefaultInstance(),
                        fullCanteen,
                        fullLargeCanteen,
                        ItemRegistry.PURIFIED_WATER_BOTTLE.get().getDefaultInstance(),
                        ItemRegistry.WATER_PLANT_BAG.get().getDefaultInstance(),
                        ItemRegistry.WATER_PLANT_SEEDS.get().getDefaultInstance(),

                        ItemRegistry.BANDAGE.get().getDefaultInstance(),
                        ItemRegistry.HEALING_HERBS.get().getDefaultInstance(),
                        ItemRegistry.MEDIKIT.get().getDefaultInstance(),
                        ItemRegistry.MORPHINE.get().getDefaultInstance(),
                        ItemRegistry.PLASTER.get().getDefaultInstance(),
                        ItemRegistry.TONIC.get().getDefaultInstance(),

                        ItemRegistry.SNOW_BOOTS.get().getDefaultInstance(),
                        ItemRegistry.SNOW_CHEST.get().getDefaultInstance(),
                        ItemRegistry.SNOW_HELMET.get().getDefaultInstance(),
                        ItemRegistry.SNOW_LEGGINGS.get().getDefaultInstance(),
                        ItemRegistry.DESERT_BOOTS.get().getDefaultInstance(),
                        ItemRegistry.DESERT_CHEST.get().getDefaultInstance(),
                        ItemRegistry.DESERT_HELMET.get().getDefaultInstance(),
                        ItemRegistry.DESERT_LEGGINGS.get().getDefaultInstance(),

                        ItemRegistry.ICE_FERN.get().getDefaultInstance(),
                        ItemRegistry.SUN_FERN.get().getDefaultInstance(),
                        ItemRegistry.ICE_FERN_SEEDS.get().getDefaultInstance(),
                        ItemRegistry.SUN_FERN_SEEDS.get().getDefaultInstance(),
                        ItemRegistry.COLD_STRING.get().getDefaultInstance(),
                        ItemRegistry.WARM_STRING.get().getDefaultInstance(),

                        ItemRegistry.COOLING_COAT_1.get().getDefaultInstance(),
                        ItemRegistry.COOLING_COAT_2.get().getDefaultInstance(),
                        ItemRegistry.COOLING_COAT_3.get().getDefaultInstance(),
                        ItemRegistry.HEATING_COAT_1.get().getDefaultInstance(),
                        ItemRegistry.HEATING_COAT_2.get().getDefaultInstance(),
                        ItemRegistry.HEATING_COAT_3.get().getDefaultInstance(),
                        ItemRegistry.THERMAL_COAT_1.get().getDefaultInstance(),
                        ItemRegistry.THERMAL_COAT_2.get().getDefaultInstance(),
                        ItemRegistry.THERMAL_COAT_3.get().getDefaultInstance(),

                        ItemRegistry.SEASONAL_CALENDAR.get().getDefaultInstance(),
                        ItemRegistry.HEART_FRUIT.get().getDefaultInstance(),
                        ItemRegistry.THERMOMETER.get().getDefaultInstance(),
                        ItemRegistry.NETHER_CHALICE.get().getDefaultInstance(),

                        BlockRegistry.COOLER.get().asItem().getDefaultInstance(),
                        BlockRegistry.HEATER.get().asItem().getDefaultInstance(),
                        BlockRegistry.SEWING_TABLE.get().asItem().getDefaultInstance()
                ));
            })
            .title(Component.translatable("itemGroup." + LegendarySurvivalOverhaul.MOD_ID))
            .build());

    public static void register(IEventBus eventBus) {
        ITEM_GROUPS.register(eventBus);
    }
}
