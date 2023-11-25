package sfiomn.legendarysurvivaloverhaul;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.CanteenProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.ThermometerProperty;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.client.screens.ThermalScreen;
import sfiomn.legendarysurvivaloverhaul.client.shaders.FocusShader;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodStorage;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierStorage;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureStorage;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstStorage;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfigRegistration;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.registry.*;
import sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal;
import sfiomn.legendarysurvivaloverhaul.util.internal.ThirstUtilInternal;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
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
	 *  so we'll probably want to integrate those with the temperature/climbing
	 *  system, i.e. making it so that winter is colder, summer is hotter,
	 *  and perhaps you're more prone to slipping while climbing in the winter.
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

	public static boolean toughAsNailsLoaded = false;
	
	public static Path configPath = FMLPaths.CONFIGDIR.get();
	public static Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "legendarysurvivaloverhaul");
	public static Path modConfigJsons = Paths.get(modConfigPath.toString(), "json");
	public static Path ssConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "sereneseasons");
	
	public static ForgeRegistry<ModifierBase> MODIFIERS;
	public static ForgeRegistry<DynamicModifierBase> DYNAMIC_MODIFIERS;

	
	public LegendarySurvivalOverhaul()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		
		modBus.addListener(this::setup);
		modBus.addListener(this::onModConfigEvent);
		modBus.addListener(this::buildRegistries);
		modBus.addListener(this::clientEvents);

		BlockRegistry.register(modBus);
		ContainerRegistry.register(modBus);
		EffectRegistry.register(modBus);
		ItemRegistry.register(modBus);
		ParticleTypeRegistry.register(modBus);
		RecipeRegistry.register(modBus);
		SoundRegistry.register(modBus);
		TemperatureModifierRegistry.register(modBus);
		TileEntityRegistry.register(modBus);
		
		forgeBus.addListener(this::serverStarted);
		forgeBus.addListener(this::reloadListener);

		forgeBus.register(this);
		
		Config.register();
		
		Config.Baked.bakeClient();
		Config.Baked.bakeCommon();
		
		TemperatureUtil.internal = new TemperatureUtilInternal();
		ThirstUtil.internal = new ThirstUtilInternal();
		modIntegration();
	}
	
	private void modIntegration()
	{
		sereneSeasonsLoaded = ModList.get().isLoaded("sereneseasons");
		curiosLoaded = ModList.get().isLoaded("curios");
		surviveLoaded = ModList.get().isLoaded("survive");
		
		if (sereneSeasonsLoaded)
			LOGGER.debug("Serene Seasons is loaded, enabling compatibility");
		if (curiosLoaded)
			LOGGER.debug("Curios is loaded, enabling compatibility");
		if (surviveLoaded)
			LOGGER.debug("Survive is loaded, I hope you know what you're doing");
	}

	@CapabilityInject(TemperatureCapability.class)
	public static final Capability<TemperatureCapability> TEMPERATURE_CAP = null;
	@CapabilityInject(TemperatureItemCapability.class)
	public static final Capability<TemperatureItemCapability> TEMPERATURE_ITEM_CAP = null;
	@CapabilityInject(HeartModifierCapability.class)
	public static final Capability<HeartModifierCapability> HEART_MOD_CAP = null;
	@CapabilityInject(WetnessCapability.class)
	public static final Capability<WetnessCapability> WETNESS_CAP = null;
	@CapabilityInject(ThirstCapability.class)
	public static final Capability<ThirstCapability> THIRST_CAP = null;
	@CapabilityInject(FoodCapability.class)
	public static final Capability<FoodCapability> FOOD_CAP = null;
	
	private void setup(final FMLCommonSetupEvent event)
	{
		CapabilityManager.INSTANCE.register(TemperatureCapability.class, new TemperatureStorage(), TemperatureCapability::new);
		CapabilityManager.INSTANCE.register(WetnessCapability.class, new WetnessCapability.Storage(), WetnessCapability::new);
		CapabilityManager.INSTANCE.register(ThirstCapability.class, new ThirstStorage(), ThirstCapability::new);
		CapabilityManager.INSTANCE.register(HeartModifierCapability.class, new HeartModifierStorage(), HeartModifierCapability::new);
		CapabilityManager.INSTANCE.register(TemperatureItemCapability.class, new TemperatureItemCapability.Storage(), TemperatureItemCapability::new);
		CapabilityManager.INSTANCE.register(FoodCapability.class, new FoodStorage(), FoodCapability::new);

		NetworkHandler.register();
		
		event.enqueueWork(EffectRegistry::registerBrewingRecipes);
	}
	
	private void clientEvents(final FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{
			RenderTypeLookup.setRenderLayer(BlockRegistry.SEWING_TABLE.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BlockRegistry.COOLER.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BlockRegistry.HEATER.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BlockRegistry.SUN_FERN.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BlockRegistry.ICE_FERN.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BlockRegistry.WATER_PLANT.get(), RenderType.cutout());

			ScreenManager.register(ContainerRegistry.COOLER_CONTAINER.get(), ThermalScreen::new);
			ScreenManager.register(ContainerRegistry.HEATER_CONTAINER.get(), ThermalScreen::new);
			ScreenManager.register(ContainerRegistry.SEWING_TABLE_CONTAINER.get(), SewingTableScreen::new);
		});

		DistExecutor.safeRunWhenOn(Dist.CLIENT, LegendarySurvivalOverhaul::clientItemPropertiesSetup);
		DistExecutor.safeRunWhenOn(Dist.CLIENT, LegendarySurvivalOverhaul::clientKeyBindingSetup);
	}
	
	private void serverStarted(final FMLServerStartedEvent event)
	{
		if (sereneSeasonsLoaded)
			SereneSeasonsModifier.prepareBiomeIdentities();
	}
	
	private static DistExecutor.SafeRunnable clientItemPropertiesSetup()
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ItemModelsProperties.register(ItemRegistry.THERMOMETER.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature"), new ThermometerProperty());
				ItemModelsProperties.register(ItemRegistry.CANTEEN.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirst_enum"), new CanteenProperty());
				ItemModelsProperties.register(ItemRegistry.LARGE_CANTEEN.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirst_enum"), new CanteenProperty());
			}
		};
	}

	private static DistExecutor.SafeRunnable clientKeyBindingSetup()
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				KeybindingRegistry.register();
			}
		};
	}
	
	private void reloadListener(final AddReloadListenerEvent event)
	{
		event.addListener(new ReloadListener<Void>() 
				{
            		@Nonnull
            		@ParametersAreNonnullByDefault
					@Override
					protected Void prepare(IResourceManager manager, IProfiler profiler)
					{
						JsonConfigRegistration.clearContainers();
						return null;
					}
            		
            		@ParametersAreNonnullByDefault
					@Override
					protected void apply(Void objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn)
					{
						JsonConfigRegistration.init(modConfigJsons.toFile());
						Config.Baked.bakeCommon();
						Config.Baked.bakeClient();
						FocusShader.loadResources();
					}
				}
		);
	}

	private void onModConfigEvent(final ModConfig.ModConfigEvent event)
	{
		final ModConfig config = event.getConfig();
		
		// Since client config is not shared, we want it to update instantly whenever it's saved
		if (config.getSpec() == Config.CLIENT_SPEC)
			Config.Baked.bakeClient();
	}
	
	// Create registries for modifiers and dynamic modifiers
	private void buildRegistries(final RegistryEvent.NewRegistry event)
	{
		RegistryBuilder<ModifierBase> modifierBuilder = new RegistryBuilder<>();
		modifierBuilder.setName(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "modifiers"));
		modifierBuilder.setType(ModifierBase.class);
		MODIFIERS = (ForgeRegistry<ModifierBase>) modifierBuilder.create();
		
		RegistryBuilder<DynamicModifierBase> dynamicModifierBuilder = new RegistryBuilder<>();
		dynamicModifierBuilder.setName(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "dynamic_modifiers"));
		dynamicModifierBuilder.setType(DynamicModifierBase.class);
		DYNAMIC_MODIFIERS = (ForgeRegistry<DynamicModifierBase>) dynamicModifierBuilder.create();
	}
}
