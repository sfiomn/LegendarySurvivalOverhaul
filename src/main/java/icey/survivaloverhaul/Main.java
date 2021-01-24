package icey.survivaloverhaul;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.logging.log4j.*;

import icey.survivaloverhaul.api.temperature.DynamicModifierBase;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierCapability;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierStorage;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureStorage;
import icey.survivaloverhaul.config.*;
import icey.survivaloverhaul.config.json.JsonConfig;
import icey.survivaloverhaul.config.json.JsonConfigRegistration;
import icey.survivaloverhaul.network.NetworkHandler;
import icey.survivaloverhaul.registry.BlockRegistry;
import icey.survivaloverhaul.registry.FeatureRegistry;
import icey.survivaloverhaul.registry.ItemRegistry;
import icey.survivaloverhaul.util.WorldUtil;
import icey.survivaloverhaul.util.internal.TemperatureUtilInternal;

@Mod(Main.MOD_ID)
@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class Main
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "survivaloverhaul";
	
	/** Serene Seasons and Better Weather both add their own seasons system,
	 *  so we'll probably want to integrate those with the temperature/climbing
	 *  system, i.e. making it so that winter is colder, summer is hotter,
	 *  and perhaps you're more prone to slipping while climbing in the winter.
	 */ 
	public static boolean betterWeatherLoaded;
	public static boolean sereneSeasonsLoaded;
	
	/**
	 * Since my mod and Survive both do very similar things, it might be
	 * a good idea to let the user know that should probably only choose
	 * one or the other unless they know what they're doing.
	 * 
	 * Also it should only show this type of warning once so that we don't
	 * annoy the player if they decide to go through with it.
	 */
	public static boolean surviveLoaded; 
	
	/**
	 * With Paragliders loaded, this mod will override the Paraglider's
	 * stamina mechanics with my own.
	 */
	public static boolean paraglidersLoaded;
	
	public static Path configPath = FMLPaths.CONFIGDIR.get();
	public static Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "survivaloverhaul");
	public static Path modConfigJsons = Paths.get(modConfigPath.toString(), "json");
	
	public static ForgeRegistry<ModifierBase> MODIFIERS;
	public static ForgeRegistry<DynamicModifierBase> DYNAMIC_MODIFIERS;
	
	public Main()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigEvent);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::buildRegistries);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		// FMLJavaModLoadingContext.get().getModEventBus().addListener(this::biomeModification);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		Config.register();
		
		Config.BakedConfigValues.bakeClient();
		Config.BakedConfigValues.bakeCommon();
		
		TemperatureUtil.internal = new TemperatureUtilInternal();
		
		sereneSeasonsLoaded = ModList.get().isLoaded("sereneseasons");
		
		if (sereneSeasonsLoaded)
				LOGGER.debug("Serene Seasons is loaded, enabling compatability");
	}
	
	@CapabilityInject(TemperatureCapability.class)
	public static final Capability<TemperatureCapability> TEMPERATURE_CAP = null;
	@CapabilityInject(HeartModifierCapability.class)
	public static final Capability<HeartModifierCapability> HEART_MOD_CAP = null;
	
	private void setup(final FMLCommonSetupEvent event)
	{
		CapabilityManager.INSTANCE.register(TemperatureCapability.class, new TemperatureStorage(), TemperatureCapability::new);
		CapabilityManager.INSTANCE.register(HeartModifierCapability.class, new HeartModifierStorage(), HeartModifierCapability::new);
		
		NetworkHandler.register();
		// FeatureRegistry.commonSetup(event);
	}
	
	@SuppressWarnings("unused")
	private void biomeModification(final BiomeLoadingEvent event)
	{
		// FeatureRegistry.biomeModification(event);
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(BlockRegistry.ModBlocks.COOLING_COIL.getBlock(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockRegistry.ModBlocks.HEATING_COIL.getBlock(), RenderType.getTranslucent());
		
		ItemModelsProperties.registerProperty(ItemRegistry.THERMOMETER, new ResourceLocation("temperature"), new IItemPropertyGetter()
				{
					@Override
					@OnlyIn(Dist.CLIENT)
					public float call(ItemStack stack, ClientWorld clientWorld, LivingEntity entity)
					{
						World world = clientWorld;
						Entity holder = (Entity) (entity != null ? entity : stack.getItemFrame());
						
						if (world == null && holder != null)
						{
							world = holder.world;
						}
						
						if (world == null)
						{
							return 0.5f;
						}
						else
						{
							double d;
							
							int temperature = WorldUtil.calculateClientWorldEntityTemperature(world, holder);
							d = (double)((float)temperature / (float)TemperatureEnum.HEAT_STROKE.getUpperBound());
							
							return MathHelper.positiveModulo((float)d, 1.0f);
						}
					}
				});
	}

    @SubscribeEvent(priority = EventPriority.LOW)
	public void reloadListener(AddReloadListenerEvent event)
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
						Config.BakedConfigValues.bakeCommon();
						JsonConfigRegistration.init(modConfigJsons.toFile());
					}
			
				});
	}
	
	private void onModConfigEvent(final ModConfig.ModConfigEvent event)
	{
		final ModConfig config = event.getConfig();
		
		if (config.getSpec() == Config.CLIENT_SPEC)
		{
			Config.BakedConfigValues.bakeClient();
		}
	}
	
	// Create registries for modifiers and dynamic modifiers
	private void buildRegistries(final RegistryEvent.NewRegistry event)
	{
		RegistryBuilder<ModifierBase> modifierBuilder = new RegistryBuilder<ModifierBase>();
		modifierBuilder.setName(new ResourceLocation(Main.MOD_ID, "modifiers"));
		modifierBuilder.setType(ModifierBase.class);
		MODIFIERS = (ForgeRegistry<ModifierBase>) modifierBuilder.create();
		
		RegistryBuilder<DynamicModifierBase> dynamicModifierBuilder = new RegistryBuilder<DynamicModifierBase>();
		dynamicModifierBuilder.setName(new ResourceLocation(Main.MOD_ID, "dynamic_modifiers"));
		dynamicModifierBuilder.setType(DynamicModifierBase.class);
		DYNAMIC_MODIFIERS = (ForgeRegistry<DynamicModifierBase>) dynamicModifierBuilder.create();
	}
}
