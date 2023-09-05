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
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureCapability.java

public class TemperatureCapability implements ITemperatureCapability
{
	private float temperature;
	private int temperatureTickTimer;
	
	//Unsaved data
	private float oldTemperature;
	private int updateTimer; //Update immediately first time around
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
	public void tickUpdate(PlayerEntity player, World world, Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}

		updateTimer++;
		if(updateTimer >= 10)
		{
			updateTimer = 0;
			targetTemp = TemperatureUtil.getPlayerTargetTemperature(player);
		}

		addTemperatureTickTimer(1);
		
		if (getTemperatureTickTimer() >= getTemperatureTickLimit()) {
			setTemperatureTickTimer(0);

			float destinationTemp = targetTemp;

			if (getTemperatureLevel() != destinationTemp) {
				tickTemperature(getTemperatureLevel(), destinationTemp);
			}

			TemperatureEnum tempEnum = getTemperatureEnum();

			if (player.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() == Items.DEBUG_STICK)
				LegendarySurvivalOverhaul.LOGGER.info(tempEnum + ", " + getTemperatureLevel() + " -> " + destinationTemp);

			if (Config.Baked.dangerousTemperature) {
				applyDangerousEffects(player, tempEnum);
			}

			if (Config.Baked.temperatureSecondaryEffects) {
				applySecondaryEffects(player, tempEnum);
			}
		}
	}

	private void applyDangerousEffects(PlayerEntity player, TemperatureEnum tempEnum) {
		if (tempEnum == TemperatureEnum.HEAT_STROKE) {
			if (TemperatureEnum.HEAT_STROKE.getMiddle() <= getTemperatureLevel() && !player.isSpectator() && !player.isCreative() && !HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply hyperthermia
				player.removeEffect(EffectRegistry.HEAT_STROKE.get());
				player.addEffect(new EffectInstance(EffectRegistry.HEAT_STROKE.get(), 300, 0, false, true));
				return;
			}
		} else if (tempEnum == TemperatureEnum.FROSTBITE) {
			if (TemperatureEnum.FROSTBITE.getMiddle() >= getTemperatureLevel() && !player.isSpectator() && !player.isCreative() && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply hypothermia
				player.removeEffect(EffectRegistry.FROSTBITE.get());
				player.addEffect(new EffectInstance(EffectRegistry.FROSTBITE.get(), 300, 0, false, true));
				return;
			}
		}
		player.removeEffect(EffectRegistry.HEAT_STROKE.get());
		player.removeEffect(EffectRegistry.FROSTBITE.get());
	}

	private void applySecondaryEffects(PlayerEntity player, TemperatureEnum tempEnum) {
		if (tempEnum == TemperatureEnum.HEAT_STROKE) {
			if (!player.isSpectator() && !player.isCreative() && !HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply secondary effect hyperthermia
				player.removeEffect(EffectRegistry.COLD_HUNGER.get());
				player.addEffect(new EffectInstance(EffectRegistry.HEAT_Thirst.get(), 300, 0, false, false));
				return;
			}
		} else if (tempEnum == TemperatureEnum.FROSTBITE) {
			if (!player.isSpectator() && !player.isCreative() && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply secondary effect hypothermia
				player.removeEffect(EffectRegistry.HEAT_Thirst.get());
				player.addEffect(new EffectInstance(EffectRegistry.COLD_HUNGER.get(), 300, 0, false, false));
				return;
			}
		}
		player.removeEffect(EffectRegistry.HEAT_Thirst.get());
		player.removeEffect(EffectRegistry.COLD_HUNGER.get());
	}
	
	private void tickTemperature(float currentTemp, float destination)
	{
		float diff = Math.abs(destination - currentTemp);
		
		float tickTowards = Math.min(1, diff);
		
		if (diff > 15)
		{
			tickTowards = 2;
		}
		
		if (getTemperatureLevel() > destination)
		{
			addTemperatureLevel(-tickTowards);
		}
		else 
		{
			addTemperatureLevel(tickTowards);
		}
	}
	
	private float getTemperatureTickLimit()
	{
		int tickMax = Config.Baked.maxTickRate;
		int tickMin = Config.Baked.minTickRate;
		
		int tickRange = tickMax - tickMin;
		
		int tempRange = TemperatureEnum.HEAT_STROKE.getUpperBound() - TemperatureEnum.FROSTBITE.getLowerBound();
		
		float currentRange = Math.abs(getTemperatureLevel() - targetTemp);
		
		return Math.max(tickMin, tickMax - ((currentRange * tickRange) / tempRange));
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
		return TemperatureUtil.getTemperatureEnum(getTemperatureLevel());
	}
	
	public CompoundNBT writeNBT() 
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putFloat("temperature", this.temperature);
		compound.putFloat("targettemperature", this.targetTemp);
		compound.putInt("ticktimer", this.temperatureTickTimer);
		
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
	}
}
