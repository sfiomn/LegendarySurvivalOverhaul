package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TemperatureUtilInternal implements ITemperatureUtil
{
	private final String COAT_TAG = "Coat";

	public static final AttributeBuilder HEATING_TEMPERATURE = new AttributeBuilder(AttributeRegistry.HEATING_TEMPERATURE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heating_temperature");
	public static final AttributeBuilder COOLING_TEMPERATURE = new AttributeBuilder(AttributeRegistry.COOLING_TEMPERATURE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cooling_temperature");
	public static final AttributeBuilder HEAT_RESISTANCE = new AttributeBuilder(AttributeRegistry.HEAT_RESISTANCE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heat_resistance");
	public static final AttributeBuilder COLD_RESISTANCE = new AttributeBuilder(AttributeRegistry.COLD_RESISTANCE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cold_resistance");
	public static final AttributeBuilder THERMAL_RESISTANCE = new AttributeBuilder(AttributeRegistry.THERMAL_RESISTANCE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".thermal_resistance");

	private static final Map<EquipmentSlotType, UUID> equipmentSlotUuid = new HashMap<>();
	static {
		equipmentSlotUuid.put(EquipmentSlotType.HEAD, UUID.fromString("06e30f27-2340-4bdb-9a91-a657f1e2880f"));
		equipmentSlotUuid.put(EquipmentSlotType.CHEST, UUID.fromString("1e7ef99e-2fe7-4edc-95b1-27fa056eae6d"));
		equipmentSlotUuid.put(EquipmentSlotType.LEGS, UUID.fromString("f46c0aff-7381-4f99-890e-75eb3781af21"));
		equipmentSlotUuid.put(EquipmentSlotType.FEET, UUID.fromString("34f98220-a7d9-4cc1-8930-b3dc4115ad07"));
		equipmentSlotUuid.put(EquipmentSlotType.MAINHAND, UUID.fromString("7b1e1c2c-746c-4631-8037-f76c82529909"));
		equipmentSlotUuid.put(EquipmentSlotType.OFFHAND, UUID.fromString("389caa2f-2c18-49da-b521-b53cc5713e14"));
	}

	@Override
	public float getPlayerTargetTemperature(PlayerEntity player)
	{
		float sum = 0.0f;
		World world = player.getCommandSenderWorld();
		BlockPos pos = WorldUtil.getSidedBlockPos(world, player);
		
		for(ModifierBase modifier : GameRegistry.findRegistry(ModifierBase.class).getValues())
		{
			float worldInfluence = modifier.getWorldInfluence(player, world, pos);
			float playerInfluence = modifier.getPlayerInfluence(player);
			if (player.getMainHandItem().getItem() == Items.DEBUG_STICK) {
				LegendarySurvivalOverhaul.LOGGER.info(modifier.getRegistryName() + " : world influence=" + worldInfluence + ", player influence=" + playerInfluence);
			}

			sum += worldInfluence + playerInfluence;
		}

		float dynamicModification = 0.0f;
		for (DynamicModifierBase dynamicModifier : GameRegistry.findRegistry(DynamicModifierBase.class).getValues())
		{
			float worldInfluence = dynamicModifier.applyDynamicWorldInfluence(player, world, pos, sum, dynamicModification);
			float playerInfluence = dynamicModifier.applyDynamicPlayerInfluence(player, sum, dynamicModification);
			if (player.getMainHandItem().getItem() == Items.DEBUG_STICK) {
				LegendarySurvivalOverhaul.LOGGER.info(dynamicModifier.getRegistryName() + " : world influence=" + worldInfluence + ", player influence=" + playerInfluence);
			}

			dynamicModification += worldInfluence + playerInfluence;
		}
		sum += dynamicModification;
		return MathUtil.round(sum, 1);
	}

	@Override
	public float getWorldTemperature(World world, BlockPos pos)
	{
		float sum = 0.0f;

		for(ModifierBase modifier : GameRegistry.findRegistry(ModifierBase.class).getValues())
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + modifier.getRegistryName() + ", " + modifier.getWorldInfluence(world, pos));
			sum += modifier.getWorldInfluence(null, world, pos);
		}

		float dynamicModification = 0.0f;
		for (DynamicModifierBase dynamicModifier : GameRegistry.findRegistry(DynamicModifierBase.class).getValues())
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + dynamicModifier.getRegistryName() + ", " + dynamicModifier.applyDynamicWorldInfluence(world, pos, sum));
			dynamicModification += dynamicModifier.applyDynamicWorldInfluence(null, world, pos, sum, dynamicModification);
		}
		sum += dynamicModification;

		return MathUtil.round(sum, 1);
	}

	@Override
	public float clampTemperature(float temperature)
	{
		return MathHelper.clamp(temperature, TemperatureEnum.FROSTBITE.getLowerBound(), TemperatureEnum.HEAT_STROKE.getUpperBound());
	}

	@Override
	public TemperatureEnum getTemperatureEnum(float temperature)
	{
		return TemperatureEnum.get(temperature);
	}

	@Override
	public boolean hasImmunity(PlayerEntity player, TemperatureImmunityEnum immunity) {
		if (!Config.Baked.temperatureEnabled)
			return false;

		TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
		return cap.getTemperatureImmunities().contains(immunity.id);
	}

	@Override
	public void addImmunity(PlayerEntity player, TemperatureImmunityEnum immunity) {
		if (!Config.Baked.temperatureEnabled)
			return;

		TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
		cap.addTemperatureImmunityId(immunity.id);
	}

	@Override
	public void removeImmunity(PlayerEntity player, TemperatureImmunityEnum immunity) {
		if (!Config.Baked.temperatureEnabled)
			return;

		TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
		cap.removeTemperatureImmunityId(immunity.id);
	}

	@Override
	public void applyItemAttributeModifiers(ItemAttributeModifierEvent event) {
		if (ItemUtil.canBeEquippedInSlot(event.getItemStack(), event.getSlotType())) {
			JsonTemperatureResistance config = new JsonTemperatureResistance();
			for (AttributeModifierBase attributeModifier : GameRegistry.findRegistry(AttributeModifierBase.class).getValues()) {
				config.add(attributeModifier.getItemAttributes(event.getItemStack()));
			}

			UUID modifierUuid = equipmentSlotUuid.get(event.getSlotType());

			if (config.temperature != 0) {
				HEATING_TEMPERATURE.addModifier(event, modifierUuid, Math.max(config.temperature, 0));
				COOLING_TEMPERATURE.addModifier(event, modifierUuid, Math.min(config.temperature, 0));
			}

			if (config.heatResistance != 0)
				HEAT_RESISTANCE.addModifier(event, modifierUuid, config.heatResistance);

			if (config.coldResistance != 0)
				COLD_RESISTANCE.addModifier(event, modifierUuid, config.coldResistance);

			if (config.thermalResistance != 0)
				THERMAL_RESISTANCE.addModifier(event, modifierUuid, config.thermalResistance);
		}
	}

	@Override
	public void addTemperatureModifier(PlayerEntity player , double temperature, UUID uuid) {
		HEATING_TEMPERATURE.addModifier(player, uuid, Math.max(temperature, 0));
		COOLING_TEMPERATURE.addModifier(player, uuid, Math.min(temperature, 0));
	}

	@Override
	public void addHeatResistanceModifier(PlayerEntity player, double resistance, UUID uuid) {
		HEAT_RESISTANCE.addModifier(player, uuid, resistance);
	}

	@Override
	public void addColdResistanceModifier(PlayerEntity player, double resistance, UUID uuid) {
		COLD_RESISTANCE.addModifier(player, uuid, resistance);
	}

	@Override
	public void addThermalResistanceModifier(PlayerEntity player, double resistance, UUID uuid) {
		THERMAL_RESISTANCE.addModifier(player, uuid, resistance);
	}

	@Override
	public void setArmorCoatTag(ItemStack stack, String coatId)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}
		
		final CompoundNBT compound = stack.getTag();

		if (compound != null) {
			compound.putString(COAT_TAG, coatId);
		}
	}

	@Override
	public String getArmorCoatTag(ItemStack stack)
	{
		if (stack.hasTag())
		{
			final CompoundNBT compound = stack.getTag();

			// TODO: remove this temporary transfer to new coat tag name
			if (compound != null && compound.contains("ArmorPadding")) {
				compound.putString(COAT_TAG, compound.getString("ArmorPadding"));
				compound.remove("ArmorPadding");
			}
			
			if (compound != null && compound.contains(COAT_TAG))
			{
				String tempTag = compound.getString(COAT_TAG);
				
				return tempTag;
			}
		}
		return "";
	}

	@Override
	public void removeArmorCoatTag(ItemStack stack)
	{
		if(stack.hasTag())
		{
			final CompoundNBT compound = stack.getTag();
			if (compound != null && compound.contains(COAT_TAG))
			{
				compound.remove(COAT_TAG);
			}
		}
	}
}
