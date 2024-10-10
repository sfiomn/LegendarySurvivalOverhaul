package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.ItemAttributeModifierEvent;
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

import static sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry.*;


public class TemperatureUtilInternal implements ITemperatureUtil
{
	public static final String COAT_TAG = "Coat";

	public static final AttributeBuilder HEATING_TEMPERATURE = new AttributeBuilder(AttributeRegistry.HEATING_TEMPERATURE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heating_temperature");
	public static final AttributeBuilder COOLING_TEMPERATURE = new AttributeBuilder(AttributeRegistry.COOLING_TEMPERATURE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cooling_temperature");
	public static final AttributeBuilder HEAT_RESISTANCE = new AttributeBuilder(AttributeRegistry.HEAT_RESISTANCE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heat_resistance");
	public static final AttributeBuilder COLD_RESISTANCE = new AttributeBuilder(AttributeRegistry.COLD_RESISTANCE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cold_resistance");
	public static final AttributeBuilder THERMAL_RESISTANCE = new AttributeBuilder(AttributeRegistry.THERMAL_RESISTANCE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".thermal_resistance");

	private static final Map<EquipmentSlot, UUID> equipmentSlotUuid = new HashMap<>();
	static {
		equipmentSlotUuid.put(EquipmentSlot.HEAD, UUID.fromString("06e30f27-2340-4bdb-9a91-a657f1e2880f"));
		equipmentSlotUuid.put(EquipmentSlot.CHEST, UUID.fromString("1e7ef99e-2fe7-4edc-95b1-27fa056eae6d"));
		equipmentSlotUuid.put(EquipmentSlot.LEGS, UUID.fromString("f46c0aff-7381-4f99-890e-75eb3781af21"));
		equipmentSlotUuid.put(EquipmentSlot.FEET, UUID.fromString("34f98220-a7d9-4cc1-8930-b3dc4115ad07"));
		equipmentSlotUuid.put(EquipmentSlot.MAINHAND, UUID.fromString("7b1e1c2c-746c-4631-8037-f76c82529909"));
		equipmentSlotUuid.put(EquipmentSlot.OFFHAND, UUID.fromString("389caa2f-2c18-49da-b521-b53cc5713e14"));
	}

	@Override
	public float getPlayerTargetTemperature(Player player)
	{
		float sum = 0.0f;
		Level world = player.getCommandSenderWorld();
		BlockPos pos = WorldUtil.getSidedBlockPos(world, player);
		
		for(ModifierBase modifier : MODIFIERS_REGISTRY.get().getValues())
		{
			float worldInfluence = modifier.getWorldInfluence(player, world, pos);
			float playerInfluence = modifier.getPlayerInfluence(player);
			if (player.getMainHandItem().is(Items.DEBUG_STICK)) {
				LegendarySurvivalOverhaul.LOGGER.info(MODIFIERS_REGISTRY.get().getKey(modifier) + " : world influence=" + worldInfluence + ", player influence=" + playerInfluence);
			}

			sum += worldInfluence + playerInfluence;
		}

		float dynamicModification = 0.0f;
		for (DynamicModifierBase dynamicModifier : DYNAMIC_MODIFIERS_REGISTRY.get().getValues())
		{
			float worldInfluence = dynamicModifier.applyDynamicWorldInfluence(player, world, pos, sum, dynamicModification);
			float playerInfluence = dynamicModifier.applyDynamicPlayerInfluence(player, sum, dynamicModification);
			if (player.getMainHandItem().is(Items.DEBUG_STICK)) {
				LegendarySurvivalOverhaul.LOGGER.info(DYNAMIC_MODIFIERS_REGISTRY.get().getKey(dynamicModifier) + " : world influence=" + worldInfluence + ", player influence=" + playerInfluence);
			}

			dynamicModification += worldInfluence + playerInfluence;
		}
		sum += dynamicModification;
		return MathUtil.round(sum, 1);
	}

	@Override
	public float getWorldTemperature(Level world, BlockPos pos)
	{
		float sum = 0.0f;

		for(ModifierBase modifier : MODIFIERS_REGISTRY.get().getValues())
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + modifier.getRegistryName() + ", " + modifier.getWorldInfluence(world, pos));
			sum += modifier.getWorldInfluence(null, world, pos);
		}

		float dynamicModification = 0.0f;
		for (DynamicModifierBase dynamicModifier : DYNAMIC_MODIFIERS_REGISTRY.get().getValues())
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
		return Mth.clamp(temperature, TemperatureEnum.FROSTBITE.getLowerBound(), TemperatureEnum.HEAT_STROKE.getUpperBound());
	}

	@Override
	public TemperatureEnum getTemperatureEnum(float temperature)
	{
		return TemperatureEnum.get(temperature);
	}

	@Override
	public boolean hasImmunity(Player player, TemperatureImmunityEnum immunity) {
		if (!Config.Baked.temperatureEnabled)
			return false;

		TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
		return cap.getTemperatureImmunities().contains(immunity.id);
	}

	@Override
	public void addImmunity(Player player, TemperatureImmunityEnum immunity) {
		if (!Config.Baked.temperatureEnabled)
			return;

		TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
		cap.addTemperatureImmunityId(immunity.id);
	}

	@Override
	public void removeImmunity(Player player, TemperatureImmunityEnum immunity) {
		if (!Config.Baked.temperatureEnabled)
			return;

		TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
		cap.removeTemperatureImmunityId(immunity.id);
	}

	@Override
	public void applyItemAttributeModifiers(ItemAttributeModifierEvent event) {
		if (ItemUtil.canBeEquippedInSlot(event.getItemStack(), event.getSlotType())) {
			JsonTemperatureResistance config = new JsonTemperatureResistance();
			for (AttributeModifierBase attributeModifier : ITEM_ATTRIBUTE_MODIFIERS_REGISTRY.get().getValues()) {
				config.add(attributeModifier.getItemAttributes(event.getItemStack()));
			}

			UUID modifierUuid = equipmentSlotUuid.get(event.getSlotType());
			HEATING_TEMPERATURE.addModifier(event, modifierUuid, Math.max(config.temperature, 0));
			COOLING_TEMPERATURE.addModifier(event, modifierUuid, Math.min(config.temperature, 0));
			HEAT_RESISTANCE.addModifier(event, modifierUuid, config.heatResistance);
			COLD_RESISTANCE.addModifier(event, modifierUuid, config.coldResistance);
			THERMAL_RESISTANCE.addModifier(event, modifierUuid, config.thermalResistance);
		}
	}

	@Override
	public void addTemperatureModifier(Player player , double temperature, UUID uuid) {
		HEATING_TEMPERATURE.addModifier(player, uuid, Math.max(temperature, 0));
		COOLING_TEMPERATURE.addModifier(player, uuid, Math.min(temperature, 0));
	}

	@Override
	public void addHeatResistanceModifier(Player player, double resistance, UUID uuid) {
		HEAT_RESISTANCE.addModifier(player, uuid, resistance);
	}

	@Override
	public void addColdResistanceModifier(Player player, double resistance, UUID uuid) {
		COLD_RESISTANCE.addModifier(player, uuid, resistance);
	}

	@Override
	public void addThermalResistanceModifier(Player player, double resistance, UUID uuid) {
		THERMAL_RESISTANCE.addModifier(player, uuid, resistance);
	}

	@Override
	public void setArmorCoatTag(ItemStack stack, String coatId)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		final CompoundTag compound = stack.getTag();

		if (compound != null) {
			compound.putString(COAT_TAG, coatId);
		}
	}

	@Override
	public String getArmorCoatTag(ItemStack stack)
	{
		if (stack.hasTag())
		{
			final CompoundTag compound = stack.getTag();

			if (compound != null && compound.contains("ArmorPadding")) {
				compound.putString(COAT_TAG, compound.getString("ArmorPadding"));
				compound.remove("ArmorPadding");
			}

			if (compound != null && compound.contains(COAT_TAG))
			{
                return compound.getString(COAT_TAG);
			}
		}
		return "";
	}

	@Override
	public void removeArmorCoatTag(ItemStack stack)
	{
		if(stack.hasTag())
		{
			final CompoundTag compound = stack.getTag();
			if (compound != null && compound.contains(COAT_TAG))
			{
				compound.remove(COAT_TAG);
			}
		}
	}
}
