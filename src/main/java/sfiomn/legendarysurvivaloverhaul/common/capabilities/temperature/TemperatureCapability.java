package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureImmunityEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureCapability.java

public class TemperatureCapability implements ITemperatureCapability
{
	private float temperature;
	private int temperatureTickTimer;
	private Set<Integer> temperatureImmunities;
	
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
		this.temperatureTickTimer = 0;
		this.temperatureImmunities = new HashSet<>();
		
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
	public void tickUpdate(Player player, Level level, Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}

		addTemperatureTickTimer(1);
		
		if (getTemperatureTickTimer() >= Config.Baked.tempTickTime) {
			setTemperatureTickTimer(0);

			this.targetTemp = TemperatureUtil.getPlayerTargetTemperature(player);

			if (getTemperatureLevel() != this.targetTemp) {
				tickTemperature(getTemperatureLevel(), this.targetTemp);
			}

			TemperatureEnum tempEnum = getTemperatureEnum();

			if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DEBUG_STICK) {
				LegendarySurvivalOverhaul.LOGGER.info(tempEnum + ", " + getTemperatureLevel() + " -> " + this.targetTemp);
			}

			applyDangerousEffects(player, tempEnum);

			applySecondaryEffects(player, tempEnum);
		}
	}

	@Override
	public void tickClient(Player player, Phase phase) {
		if(phase == TickEvent.Phase.START) {
			return;
		}

		if (getTemperatureEnum() == TemperatureEnum.FROSTBITE && !FrostbiteEffect.playerIsImmuneToFrost(player))
			shakePlayer(player);
	}

	private void applyDangerousEffects(Player player, TemperatureEnum tempEnum) {
		if (Config.Baked.dangerousHeatTemperature && ThirstUtil.isThirstActive(player) && tempEnum == TemperatureEnum.HEAT_STROKE) {
			if (TemperatureEnum.HEAT_STROKE.getMiddle() <= getTemperatureLevel() && !HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply hyperthermia
				if (!player.hasEffect(MobEffectRegistry.HEAT_STROKE.get()))
					player.addEffect(new MobEffectInstance(MobEffectRegistry.HEAT_STROKE.get(), -1, 0, false, true));
				return;
			}
		} else if (Config.Baked.dangerousColdTemperature && tempEnum == TemperatureEnum.FROSTBITE) {
			if (TemperatureEnum.FROSTBITE.getMiddle() >= getTemperatureLevel() && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply hypothermia.json
				if (!player.hasEffect(MobEffectRegistry.FROSTBITE.get()))
					player.addEffect(new MobEffectInstance(MobEffectRegistry.FROSTBITE.get(), -1, 0, false, true));
				return;
			}
		}
		if (player.hasEffect(MobEffectRegistry.HEAT_STROKE.get()))
			player.removeEffect(MobEffectRegistry.HEAT_STROKE.get());
		if (player.hasEffect(MobEffectRegistry.FROSTBITE.get()))
			player.removeEffect(MobEffectRegistry.FROSTBITE.get());
	}

	private void applySecondaryEffects(Player player, TemperatureEnum tempEnum) {
		if (Config.Baked.heatTemperatureSecondaryEffects && tempEnum == TemperatureEnum.HEAT_STROKE) {
			if (!HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply secondary effect hyperthermia
				if (!player.hasEffect(MobEffectRegistry.HEAT_THIRST.get()))
					player.addEffect(new MobEffectInstance(MobEffectRegistry.HEAT_THIRST.get(), -1, 0, false, false));
				return;
			}
		} else if (Config.Baked.coldTemperatureSecondaryEffects && tempEnum == TemperatureEnum.FROSTBITE) {
			if (!FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply secondary effect hypothermia
				if (!player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()))
					player.addEffect(new MobEffectInstance(MobEffectRegistry.COLD_HUNGER.get(), -1, 0, false, false));
				return;
			}
		}
		if (player.hasEffect(MobEffectRegistry.HEAT_THIRST.get()))
			player.removeEffect(MobEffectRegistry.HEAT_THIRST.get());
		if (player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()))
			player.removeEffect(MobEffectRegistry.COLD_HUNGER.get());
	}

	private void shakePlayer(Player player) {
		player.setYBodyRot(player.getYRot() + (float) (Math.cos((double) player.tickCount * 3.25D) * Math.PI * (double) 0.4F));
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

	public CompoundTag writeNBT() 
	{
		CompoundTag compound = new CompoundTag();
		
		compound.putFloat("temperature", this.temperature);
		compound.putFloat("targettemperature", this.targetTemp);
		compound.putInt("ticktimer", this.temperatureTickTimer);
		compound.putIntArray("immunities", this.getTemperatureImmunities());
		
		return compound;
	}
	
	public void readNBT(CompoundTag compound)
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
