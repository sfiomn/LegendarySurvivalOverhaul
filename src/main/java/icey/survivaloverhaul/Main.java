package icey.survivaloverhaul;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import org.apache.logging.log4j.*;

import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureStorage;
import icey.survivaloverhaul.common.tempmods.DynamicModifierBase;
import icey.survivaloverhaul.common.tempmods.ModifierBase;
import icey.survivaloverhaul.config.*;
import icey.survivaloverhaul.network.NetworkHandler;
import icey.survivaloverhaul.setup.BlockRegistry;
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
	
	public static ForgeRegistry<ModifierBase> MODIFIERS;
	public static ForgeRegistry<DynamicModifierBase> DYNAMIC_MODIFIERS;
	
	public Main()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigEvent);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::buildRegistries);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		Config.register();
		
		Config.BakedConfigValues.bakeClient();
		Config.BakedConfigValues.bakeCommon();
		
		TemperatureUtil.internal = new TemperatureUtilInternal();
	}
	
	@CapabilityInject(TemperatureCapability.class)
	public static final Capability<TemperatureCapability> TEMPERATURE_CAP = null;
	
	private void setup(final FMLCommonSetupEvent event)
	{
		CapabilityManager.INSTANCE.register(TemperatureCapability.class, new TemperatureStorage(), TemperatureCapability::new);
		NetworkHandler.register();
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(BlockRegistry.ModBlocks.COOLING_COIL.getBlock(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockRegistry.ModBlocks.HEATING_COIL.getBlock(), RenderType.getTranslucent());
	}
	
	private void onModConfigEvent(final ModConfig.ModConfigEvent event)
	{
		final ModConfig config = event.getConfig();
		
		if (config.getSpec() == Config.CLIENT_SPEC)
		{
			Config.BakedConfigValues.bakeClient();
		}
		else if (config.getSpec() == Config.COMMON_SPEC)
		{
			Config.BakedConfigValues.bakeCommon();
		}
	}
	
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
