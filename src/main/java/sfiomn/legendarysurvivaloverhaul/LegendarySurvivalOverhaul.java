package sfiomn.legendarysurvivaloverhaul;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.CanteenProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.SeasonalCalendarTimeProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.SeasonalCalendarSeasonTypeProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.ThermometerProperty;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.client.screens.ThermalScreen;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfigRegistration;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.registry.*;
import sfiomn.legendarysurvivaloverhaul.util.internal.BodyDamageUtilInternal;
import sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal;
import sfiomn.legendarysurvivaloverhaul.util.internal.ThirstUtilInternal;

import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
@Mod(LegendarySurvivalOverhaul.MOD_ID)
@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID)
public class LegendarySurvivalOverhaul
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "legendarysurvivaloverhaul";
	
	/** Serene Seasons and Better Weather both add their own seasons system,
	 *  so we'll probably want to integrate those with the temperature,
	 *  i.e. making it so that winter is colder, summer is hotter.
	 */ 
	public static boolean betterWeatherLoaded = false;
	public static boolean sereneSeasonsLoaded = false;
	
	/**
	 * Since my mod and Survive both do very similar things, it might be
	 * a good idea to let the user know that should probably only choose
	 * one or the other unless they know what they're doing,
	 * Also it should only show this type of warning once so that we don't
	 * annoy the player if they decide to go through with it.
	 */
	public static boolean surviveLoaded = false;
	public static boolean curiosLoaded = false;
	public static boolean feathersLoaded = false;
	
	public static Path configPath = FMLPaths.CONFIGDIR.get();
	public static Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "legendarysurvivaloverhaul");
	public static Path modConfigJsons = Paths.get(modConfigPath.toString(), "json");
	
	public LegendarySurvivalOverhaul()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		
		modBus.addListener(this::setup);
		modBus.addListener(this::onModConfigLoadEvent);
		modBus.addListener(this::onModConfigReloadEvent);
		modBus.addListener(this::clientEvents);

		ItemRegistry.register(modBus);
		BlockRegistry.register(modBus);
		ContainerRegistry.register(modBus);
		MobEffectRegistry.register(modBus);
		ParticleTypeRegistry.register(modBus);
		RecipeRegistry.register(modBus);
		SoundRegistry.register(modBus);
		TemperatureModifierRegistry.register(modBus);
		BlockEntityRegistry.register(modBus);
		CreativeTabRegistry.register(modBus);

		forgeBus.addListener(CommandRegistry::registerCommandsEvent);
		forgeBus.addListener(this::reloadListener);
		forgeBus.addListener(this::registerCapabilities);

		forgeBus.register(this);
		
		Config.register();
		
		TemperatureUtil.internal = new TemperatureUtilInternal();
		ThirstUtil.internal = new ThirstUtilInternal();
		BodyDamageUtil.internal = new BodyDamageUtilInternal();
		modIntegration();
	}
	
	private void modIntegration()
	{
		sereneSeasonsLoaded = ModList.get().isLoaded("sereneseasons");
		curiosLoaded = ModList.get().isLoaded("curios");
		surviveLoaded = ModList.get().isLoaded("survive");
		feathersLoaded = ModList.get().isLoaded("feathers");
		
		if (sereneSeasonsLoaded)
			LOGGER.debug("Serene Seasons is loaded, enabling compatibility");
		if (curiosLoaded)
			LOGGER.debug("Curios is loaded, enabling compatibility");
		if (surviveLoaded)
			LOGGER.debug("Survive is loaded, I hope you know what you're doing");
		if (feathersLoaded)
			LOGGER.debug("Feathers is loaded, enabling compatibility");
	}
	
	private void setup(final FMLCommonSetupEvent event)
	{
		NetworkHandler.register();

		Config.Baked.bakeCommon();

		event.enqueueWork(MobEffectRegistry::registerBrewingRecipes);

		BodyDamageUtilInternal.initMalusConfig();
	}
	
	private void clientEvents(final FMLClientSetupEvent event)
	{
		Config.Baked.bakeClient();

		DistExecutor.safeRunWhenOn(Dist.CLIENT, LegendarySurvivalOverhaul::clientItemPropertiesSetup);

		MenuScreens.register(ContainerRegistry.COOLER_CONTAINER.get(), ThermalScreen::new);
		MenuScreens.register(ContainerRegistry.HEATER_CONTAINER.get(), ThermalScreen::new);
		MenuScreens.register(ContainerRegistry.SEWING_TABLE_CONTAINER.get(), SewingTableScreen::new);
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(TemperatureCapability.class);
		event.register(WetnessCapability.class);
		event.register(ThirstCapability.class);
		event.register(HeartModifierCapability.class);
		event.register(TemperatureItemCapability.class);
		event.register(FoodCapability.class);
		event.register(BodyDamageCapability.class);
	}
	
	private static DistExecutor.SafeRunnable clientItemPropertiesSetup()
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ItemProperties.register(ItemRegistry.THERMOMETER.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature"), new ThermometerProperty());
				ItemProperties.register(ItemRegistry.CANTEEN.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirstenum"), new CanteenProperty());
				ItemProperties.register(ItemRegistry.LARGE_CANTEEN.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirstenum"), new CanteenProperty());
				if (sereneSeasonsLoaded) {
					ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "time"), new SeasonalCalendarTimeProperty());
					ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "seasontype"), new SeasonalCalendarSeasonTypeProperty());
				}
			}
		};
	}
	
	private void reloadListener(AddReloadListenerEvent event)
	{
		event.addListener(new SimplePreparableReloadListener<>() {
                              @Override
                              protected Object prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
                                  JsonConfigRegistration.clearContainers();
                                  return null;
                              }

                              @Override
                              protected void apply(Object o, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
                                  JsonConfigRegistration.init(modConfigJsons.toFile());
                                  Config.Baked.bakeCommon();
                              }
                          }
		);
	}

	private void onModConfigLoadEvent(ModConfigEvent.Loading event)
	{
		final ModConfig config = event.getConfig();

		if (config.getSpec() == Config.CLIENT_SPEC)
			Config.Baked.bakeClient();
	}

	private void onModConfigReloadEvent(ModConfigEvent.Reloading event)
	{
		final ModConfig config = event.getConfig();

		// Since client config is not shared, we want it to update instantly whenever it's saved
		if (config.getSpec() == Config.CLIENT_SPEC)
			Config.Baked.bakeClient();
	}
}
