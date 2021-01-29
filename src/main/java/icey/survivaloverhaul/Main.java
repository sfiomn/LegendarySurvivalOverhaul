package icey.survivaloverhaul;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.client.settings.KeyBinding;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.logging.log4j.*;
import org.lwjgl.glfw.GLFW;

import icey.survivaloverhaul.api.temperature.DynamicModifierBase;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierCapability;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierStorage;
import icey.survivaloverhaul.common.capability.stamina.StaminaCapability;
import icey.survivaloverhaul.common.capability.stamina.StaminaStorage;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureStorage;
import icey.survivaloverhaul.config.*;
import icey.survivaloverhaul.config.json.JsonConfigRegistration;
import icey.survivaloverhaul.network.NetworkHandler;
import icey.survivaloverhaul.registry.BlockRegistry;
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
	public static boolean curiosLoaded;
	
	public static Path configPath = FMLPaths.CONFIGDIR.get();
	public static Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "survivaloverhaul");
	public static Path modConfigJsons = Paths.get(modConfigPath.toString(), "json");
	
	public static ForgeRegistry<ModifierBase> MODIFIERS;
	public static ForgeRegistry<DynamicModifierBase> DYNAMIC_MODIFIERS;
	
	//@OnlyIn(Dist.CLIENT)//broke on server, no longer using const :(
	//public static final KeyBinding KEY_CLIMB = new KeyBinding("key." + MOD_ID + ".grab", GLFW.GLFW_KEY_R, "key.categories.inventory");
	
	public Main()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigEvent);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::buildRegistries);
		// FMLJavaModLoadingContext.get().getModEventBus().addListener(this::biomeModification);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientEvents);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setup);
		bus.addListener(this::onModConfigEvent);
		bus.addListener(this::buildRegistries);
		bus.addListener(this::clientEvents);
		
		
		MinecraftForge.EVENT_BUS.register(this);
		
		Config.register();
		
		Config.Baked.bakeClient();
		Config.Baked.bakeCommon();
		
		TemperatureUtil.internal = new TemperatureUtilInternal();
		
		sereneSeasonsLoaded = ModList.get().isLoaded("sereneseasons");
		
		if (sereneSeasonsLoaded)
				LOGGER.debug("Serene Seasons is loaded, enabling compatability");
		//System.out.println("Hello from " + MOD_ID);
	}
	
	@CapabilityInject(TemperatureCapability.class)
	public static final Capability<TemperatureCapability> TEMPERATURE_CAP = null;
	@CapabilityInject(HeartModifierCapability.class)
	public static final Capability<HeartModifierCapability> HEART_MOD_CAP = null;
	@CapabilityInject(StaminaCapability.class)
	public static final Capability<StaminaCapability> STAMINA_CAP = null;
	
	private void setup(final FMLCommonSetupEvent event)
	{
		CapabilityManager.INSTANCE.register(TemperatureCapability.class, new TemperatureStorage(), TemperatureCapability::new);
		CapabilityManager.INSTANCE.register(HeartModifierCapability.class, new HeartModifierStorage(), HeartModifierCapability::new);
		CapabilityManager.INSTANCE.register(StaminaCapability.class, new StaminaStorage(), StaminaCapability::new);
		
		NetworkHandler.register();
		//FeatureRegistry.commonSetup(event);
	}
	
	@SuppressWarnings("unused")
	private void biomeModification(final BiomeLoadingEvent event)
	{
		// FeatureRegistry.biomeModification(event);
	}
	
	private void clientEvents(final FMLClientSetupEvent event)
	{
		event.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientModelSetup()));
		event.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientKeyBindsSetup()));
	}
	
	@SubscribeEvent
	public static void serverStarted(FMLServerStartedEvent event)
	{
		if (Config.COMMON.forceDisableFlightKick.get())
		{
			event.getServer().setAllowFlight(true);
		}
	}
	
	private static DistExecutor.SafeRunnable clientModelSetup()
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				RenderTypeLookup.setRenderLayer(BlockRegistry.ModBlocks.COOLING_COIL.getBlock(), RenderType.getCutout());
				RenderTypeLookup.setRenderLayer(BlockRegistry.ModBlocks.HEATING_COIL.getBlock(), RenderType.getCutout());
				
				ItemModelsProperties.registerProperty(ItemRegistry.THERMOMETER, new ResourceLocation("temperature"), new IItemPropertyGetter()
						{
							@OnlyIn(Dist.CLIENT)
							@Override
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
									try
									{
										double d;
										
										int temperature = WorldUtil.calculateClientWorldEntityTemperature(world, holder);
										d = (double)((float)temperature / (float)TemperatureEnum.HEAT_STROKE.getUpperBound());
										
										return MathHelper.positiveModulo((float)d, 1.0333333f);
									}
									catch (NullPointerException e)
									{
										return 0.5f;
									}
									
								}
							}
						}
				);
			}
		};
	}
	
	public static KeyBinding KEY_CLIMB;
	
	private static DistExecutor.SafeRunnable clientKeyBindsSetup()
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				ClientRegistry.registerKeyBinding(new KeyBinding("key." + MOD_ID + ".grab", GLFW.GLFW_KEY_R, "key.categories.inventory"));
				KEY_CLIMB = new KeyBinding("key." + MOD_ID + ".grab", GLFW.GLFW_KEY_R, "key.categories.inventory");
			}
		};
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
						Config.Baked.bakeCommon();
						JsonConfigRegistration.init(modConfigJsons.toFile());
					}
			
				});
	}
	
	private void onModConfigEvent(final ModConfig.ModConfigEvent event)
	{
		final ModConfig config = event.getConfig();
		
		// Since client config is not shared, we want it to update instantly whenever it's saved
		if (config.getSpec() == Config.CLIENT_SPEC)
		{
			Config.Baked.bakeClient();
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
