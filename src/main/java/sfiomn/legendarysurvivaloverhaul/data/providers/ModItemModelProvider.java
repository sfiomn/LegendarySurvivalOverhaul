package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LegendarySurvivalOverhaul.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleJuiceItem(ItemRegistry.APPLE_JUICE);
        simpleJuiceItem(ItemRegistry.BEETROOT_JUICE);
        simpleJuiceItem(ItemRegistry.CACTUS_JUICE);
        simpleJuiceItem(ItemRegistry.CARROT_JUICE);
        simpleJuiceItem(ItemRegistry.CHORUS_FRUIT_JUICE);
        simpleJuiceItem(ItemRegistry.GLISTERING_MELON_JUICE);
        simpleJuiceItem(ItemRegistry.GOLDEN_APPLE_JUICE);
        simpleJuiceItem(ItemRegistry.GOLDEN_CARROT_JUICE);
        simpleJuiceItem(ItemRegistry.MELON_JUICE);
        simpleJuiceItem(ItemRegistry.PUMPKIN_JUICE);
        simpleJuiceItem(ItemRegistry.GOLDEN_APPLE_JUICE);
        simpleJuiceItem(ItemRegistry.GOLDEN_CARROT_JUICE);

        simpleHealingItem(ItemRegistry.BANDAGE);
        simpleHealingItem(ItemRegistry.HEALING_HERBS);
        simpleHealingItem(ItemRegistry.MEDIKIT);
        simpleHealingItem(ItemRegistry.MORPHINE);
        simpleHealingItem(ItemRegistry.PLASTER);
        simpleHealingItem(ItemRegistry.TONIC);

        canteenItem(ItemRegistry.CANTEEN);
        canteenItem(ItemRegistry.LARGE_CANTEEN);

        simpleItem(ItemRegistry.PURIFIED_WATER_BOTTLE);
        simpleItem(ItemRegistry.ICE_FERN);
        simpleItem(ItemRegistry.HEART_FRUIT);
        simpleItem(ItemRegistry.SUN_FERN);
        simpleItem(ItemRegistry.WATER_PLANT_BAG);
        simpleItem(ItemRegistry.COLD_STRING);
        simpleItem(ItemRegistry.HEART_FRUIT);
        simpleItem(ItemRegistry.WARM_STRING);

        singleTexture("water_plant", new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "block/water_plant_top"));

        simpleCoatItem(ItemRegistry.COOLING_COAT_1);
        simpleCoatItem(ItemRegistry.COOLING_COAT_2);
        simpleCoatItem(ItemRegistry.COOLING_COAT_3);
        simpleCoatItem(ItemRegistry.HEATING_COAT_1);
        simpleCoatItem(ItemRegistry.HEATING_COAT_2);
        simpleCoatItem(ItemRegistry.HEATING_COAT_3);
        simpleCoatItem(ItemRegistry.THERMAL_COAT_1);
        simpleCoatItem(ItemRegistry.THERMAL_COAT_2);
        simpleCoatItem(ItemRegistry.THERMAL_COAT_3);

        simpleArmorItem(ItemRegistry.DESERT_BOOTS);
        simpleArmorItem(ItemRegistry.DESERT_CHEST);
        simpleArmorItem(ItemRegistry.DESERT_HELMET);
        simpleArmorItem(ItemRegistry.DESERT_LEGGINGS);
        simpleArmorItem(ItemRegistry.SNOW_BOOTS);
        simpleArmorItem(ItemRegistry.SNOW_CHEST);
        simpleArmorItem(ItemRegistry.SNOW_HELMET);
        simpleArmorItem(ItemRegistry.SNOW_LEGGINGS);

        for (int i=0; i<29; i++) {
            String jsonName = String.format("%02d", i);
            singleTexture("item/thermometer/thermometer_" + jsonName, new ResourceLocation("item/generated"),
                    "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/thermometer/thermometer_" + jsonName));
        }

        String[] seasons = {"autumn", "dry", "spring", "summer", "wet", "winter"};
        for (int i=1; i<4; i++) {
            for (String season: seasons) {
                String jsonName = season + i;
                singleTexture("item/seasonal_calendar/seasonal_calendar_" + jsonName, new ResourceLocation("item/generated"),
                        "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/seasonal_calendar/" + jsonName));
            }
        }

        singleTexture("item/seasonal_calendar/seasonal_calendar_null", new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/seasonal_calendar/calendar_null"));
    }

    private void simpleItem(RegistryObject<Item> item) {
        singleTexture(item.getId().getPath(), new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void simpleItem(String jsonName) {
        singleTexture(jsonName, new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/" + jsonName));
    }

    private void simpleArmorItem(RegistryObject<Item> item) {
        singleTexture(item.getId().getPath(), new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/armor/" + item.getId().getPath()));
    }

    private void simpleCoatItem(RegistryObject<Item> item) {
        singleTexture(item.getId().getPath(), new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/coat/" + item.getId().getPath()));
    }

    private void simpleHealingItem(RegistryObject<Item> item) {
        singleTexture(item.getId().getPath(), new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/healing/" + item.getId().getPath()));
    }

    private void simpleJuiceItem(RegistryObject<Item> item) {
        singleTexture(item.getId().getPath(), new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/juice/" + item.getId().getPath()));
    }

    private void simplePaddingItem(RegistryObject<Item> item) {
        singleTexture(item.getId().getPath(), new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/padding/" + item.getId().getPath()));
    }

    private void canteenItem(RegistryObject<Item> canteenItem) {
        withExistingParent(canteenItem.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/" + canteenItem.getId().getPath()))
                .override()
                .predicate(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirst_enum"), 3)
                .model(new ModelFile.UncheckedModelFile(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item/" + canteenItem.getId().getPath() + "_purified")))
                .end();

        simpleItem(canteenItem.getId().getPath() + "_purified");
    }
}
