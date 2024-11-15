package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;

import java.util.*;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureCapability.java

public class TemperatureCapability implements ITemperatureCapability
{
	private float temperature;
	private Set<Integer> temperatureImmunities;
	private int temperatureTickTimer;
	
	//Unsaved data
	private float oldTemperature;
	private float targetTemp;
	private boolean manualDirty;
	private int packetTimer;
	
	public TemperatureCapability() 
	{
		this.init();
	}
	
	public void init()
	{
		this.temperature = TemperatureEnum.NORMAL.getMiddle();
		this.temperatureImmunities = new HashSet<>();
		this.temperatureTickTimer = 0;
		
		this.oldTemperature = 0;
		this.targetTemp = 0;
		this.manualDirty = false;
		this.packetTimer = 0;
	}
	
	@Override
	public float getTemperatureLevel()
	{
		return temperature;
	}

	@Override
	public float getTargetTemperatureLevel()
	{
		return targetTemp;
	}

	@Override
	public int getTemperatureTickTimer()
	{
		return temperatureTickTimer;
	}

	@Override
	public void setTemperatureLevel(float temperature)
	{
		this.temperature = temperature;
	}

	@Override
	public void setTargetTemperatureLevel(float targetTemperature) {
		this.targetTemp = targetTemperature;
	}

	@Override
	public void setTemperatureTickTimer(int tickTimer)
	{
		this.temperatureTickTimer = tickTimer;
	}

	@Override
	public void addTemperatureLevel(float temperature)
	{
		this.setTemperatureLevel(getTemperatureLevel() + temperature);
	}

	@Override
	public void addTemperatureTickTimer(int tickTimer)
	{
		this.setTemperatureTickTimer(this.temperatureTickTimer + tickTimer);
	}

	@Override
	public void addTemperatureImmunityId(int immunityId) {
		this.temperatureImmunities.add(immunityId);
	}

	@Override
	public void removeTemperatureImmunityId(int immunityId) {
		this.temperatureImmunities.remove(immunityId);
	}

	@Override
	public void tickUpdate(PlayerEntity player, World world, Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}

		addTemperatureTickTimer(1);
		
		if (getTemperatureTickTimer() >= Config.Baked.tempTickTime) {
			setTemperatureTickTimer(0);

			targetTemp = TemperatureUtil.getPlayerTargetTemperature(player);

			if (getTemperatureLevel() != targetTemp) {
				tickTemperature(getTemperatureLevel(), targetTemp);
			}

			TemperatureEnum tempEnum = getTemperatureEnum();

			if (player.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() == Items.DEBUG_STICK)
				LegendarySurvivalOverhaul.LOGGER.info(tempEnum + ", " + getTemperatureLevel() + " -> " + targetTemp);

			applyDangerousEffects(player, tempEnum);

			applySecondaryEffects(player, tempEnum);
		}
	}

	@Override
	public void tickClient(PlayerEntity player, Phase phase) {
		if(phase == TickEvent.Phase.START) {
			return;
		}

		if (getTemperatureEnum() == TemperatureEnum.FROSTBITE && !FrostbiteEffect.playerIsImmuneToFrost(player))
			shakePlayer(player);
	}

	private void applyDangerousEffects(PlayerEntity player, TemperatureEnum tempEnum) {
		if (Config.Baked.dangerousHeatTemperature && ThirstUtil.isThirstActive(player) && tempEnum == TemperatureEnum.HEAT_STROKE) {
			if (TemperatureEnum.HEAT_STROKE.getMiddle() <= getTemperatureLevel() && !HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply hyperthermia
				if (!player.hasEffect(EffectRegistry.HEAT_STROKE.get()))
					player.addEffect(new EffectInstance(EffectRegistry.HEAT_STROKE.get(), 1000, 0, false, true));
				return;
			}
		} else if (Config.Baked.dangerousColdTemperature && tempEnum == TemperatureEnum.FROSTBITE) {
			if (TemperatureEnum.FROSTBITE.getMiddle() >= getTemperatureLevel() && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply hypothermia
				if (!player.hasEffect(EffectRegistry.FROSTBITE.get()))
					player.addEffect(new EffectInstance(EffectRegistry.FROSTBITE.get(), 1000, 0, false, true));
				return;
			}
		}
		if (player.hasEffect(EffectRegistry.HEAT_STROKE.get()))
			player.removeEffect(EffectRegistry.HEAT_STROKE.get());
		if (player.hasEffect(EffectRegistry.FROSTBITE.get()))
			player.removeEffect(EffectRegistry.FROSTBITE.get());
	}

	private void applySecondaryEffects(PlayerEntity player, TemperatureEnum tempEnum) {
		if (Config.Baked.heatTemperatureSecondaryEffects && tempEnum == TemperatureEnum.HEAT_STROKE) {
			if (!HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply secondary effect hyperthermia
				player.removeEffect(EffectRegistry.COLD_HUNGER.get());
				player.addEffect(new EffectInstance(EffectRegistry.HEAT_THIRST.get(), 300, 0, false, false));
				return;
			}
		} else if (Config.Baked.coldTemperatureSecondaryEffects && tempEnum == TemperatureEnum.FROSTBITE) {
			if (!FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply secondary effect hypothermia
				player.removeEffect(EffectRegistry.HEAT_THIRST.get());
				player.addEffect(new EffectInstance(EffectRegistry.COLD_HUNGER.get(), 300, 0, false, false));
				return;
			}
		}
		if (player.hasEffect(EffectRegistry.HEAT_THIRST.get()))
			player.removeEffect(EffectRegistry.HEAT_THIRST.get());
		if (player.hasEffect(EffectRegistry.COLD_HUNGER.get()))
			player.removeEffect(EffectRegistry.COLD_HUNGER.get());
	}

	private void shakePlayer(PlayerEntity player) {
		player.setYBodyRot(player.yBodyRot + (float) (Math.cos((double) player.tickCount * 3.25D) * Math.PI * (double) 0.4F));
	}
	
	private void tickTemperature(float currentTemp, float destination)
	{
		float diff = Math.abs(destination - currentTemp);
		
		double temperatureTowards = ((diff * (Config.Baked.maxTemperatureModification - Config.Baked.minTemperatureModification)) / (TemperatureEnum.HEAT_STROKE.getUpperBound() - TemperatureEnum.FROSTBITE.getLowerBound())) + Config.Baked.minTemperatureModification;

		temperatureTowards = Math.min(temperatureTowards, diff);
		
		if (currentTemp > destination)
		{
			addTemperatureLevel((float) -temperatureTowards);
		}
		else 
		{
			addTemperatureLevel((float) temperatureTowards);
		}
	}

	@Override
	public boolean isDirty()
	{
		return manualDirty || this.temperature != this.oldTemperature;
	}

	@Override
	public void setClean()
	{
		this.oldTemperature = this.temperature;
		this.manualDirty = false;
	}

	@Override
	public int getPacketTimer()
	{
		return packetTimer;
	}

	@Override
	public TemperatureEnum getTemperatureEnum()
	{
		return TemperatureEnum.get(temperature);
	}

	@Override
	public List<Integer> getTemperatureImmunities() {
		return new ArrayList<>(this.temperatureImmunities);
	}

	public CompoundNBT writeNBT() 
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putFloat("temperature", this.temperature);
		compound.putFloat("targettemperature", this.targetTemp);
		compound.putInt("ticktimer", this.temperatureTickTimer);
		compound.putIntArray("immunities", this.getTemperatureImmunities());
		
		return compound;
	}
	
	public void readNBT(CompoundNBT compound)
	{
		this.init();
		if (compound.contains("temperature"))
			this.setTemperatureLevel(compound.getFloat("temperature"));
		if (compound.contains("targettemperature"))
			this.setTargetTemperatureLevel(compound.getFloat("targettemperature"));
		if (compound.contains("tickTimer"))
			this.setTemperatureTickTimer(compound.getInt("tickTimer"));
		if (compound.contains("immunities"))
			for (int immunityId: compound.getIntArray("immunities"))
				this.addTemperatureImmunityId(immunityId);
	}
}
