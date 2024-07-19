package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import com.elenai.feathers.effect.FeathersEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.Objects;

import static sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul.feathersLoaded;

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
	private int soundTriggerTick;
	
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
		this.soundTriggerTick = 0;
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
	public void tickUpdate(Player player, Level level, Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}

		if(updateTimer++ >= 10)
		{
			updateTimer = 0;
			targetTemp = TemperatureUtil.getPlayerTargetTemperature(player);
		}

		addTemperatureTickTimer(1);

		if (this.soundTriggerTick > 0)
			this.soundTriggerTick--;
		
		if (getTemperatureTickTimer() >= Config.Baked.tickRate) {
			setTemperatureTickTimer(0);

			float destinationTemp = targetTemp;

			if (getTemperatureLevel() != destinationTemp) {
				tickTemperature(getTemperatureLevel(), destinationTemp);
			}

			TemperatureEnum tempEnum = getTemperatureEnum();

			if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DEBUG_STICK)
				LegendarySurvivalOverhaul.LOGGER.info(tempEnum + ", " + getTemperatureLevel() + " -> " + destinationTemp);

			if (Config.Baked.dangerousTemperature)
				applyDangerousEffects(player);

			if (Config.Baked.temperatureSecondaryEffects)
				applySecondaryEffects(player);
		}
	}

	private void applyDangerousEffects(Player player) {
		if (getTemperatureEnum() == TemperatureEnum.HEAT_STROKE) {
			if (TemperatureEnum.HEAT_STROKE.getMiddle() <= getTemperatureLevel() && !player.isSpectator() && !player.isCreative() && !HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply hyperthermia
				if (!player.hasEffect(MobEffectRegistry.HEAT_STROKE.get()))
					player.addEffect(new MobEffectInstance(MobEffectRegistry.HEAT_STROKE.get(), 1000, 0, false, true));
				return;
			}
		} else if (getTemperatureEnum() == TemperatureEnum.FROSTBITE) {
			if (TemperatureEnum.FROSTBITE.getMiddle() >= getTemperatureLevel() && !player.isSpectator() && !player.isCreative() && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply hypothermia.json
				if (!player.hasEffect(MobEffectRegistry.FROSTBITE.get()))
					player.addEffect(new MobEffectInstance(MobEffectRegistry.FROSTBITE.get(), 1000, 0, false, true));
				return;
			}
		}
		player.removeEffect(MobEffectRegistry.HEAT_STROKE.get());
		player.removeEffect(MobEffectRegistry.FROSTBITE.get());
	}

	private void applySecondaryEffects(Player player) {
		if (getTemperatureEnum() == TemperatureEnum.HEAT_STROKE) {
			if (!player.isSpectator() && !player.isCreative() && !HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply secondary effect hyperthermia
				player.removeEffect(MobEffectRegistry.COLD_HUNGER.get());
				player.addEffect(new MobEffectInstance(MobEffectRegistry.HEAT_Thirst.get(), 300, 0, false, false));
				return;
			}
		} else if (getTemperatureEnum() == TemperatureEnum.FROSTBITE) {
			if (!player.isSpectator() && !player.isCreative() && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply secondary effect hypothermia
				player.removeEffect(MobEffectRegistry.HEAT_Thirst.get());
				player.addEffect(new MobEffectInstance(MobEffectRegistry.COLD_HUNGER.get(), 300, 0, false, false));

				//  Waiting a fix from Feathers to remove the sound triggered when the Cold effect is applied
				//  Current workaround is to directly use the effect update method, but doesn't refresh the client side
				if (feathersLoaded)
					if (player.hasEffect(FeathersEffects.COLD.get())) {
						Objects.requireNonNull(player.getEffect(FeathersEffects.COLD.get())).update(new MobEffectInstance(FeathersEffects.COLD.get(), 300, 0, false, true));
					} else
						player.addEffect(new MobEffectInstance(FeathersEffects.COLD.get(), 300, 0, false, true));
				return;
			}
		}
		player.removeEffect(MobEffectRegistry.HEAT_Thirst.get());
		player.removeEffect(MobEffectRegistry.COLD_HUNGER.get());
	}

	public void shakePlayer(Player player) {
		if (getTemperatureEnum() == TemperatureEnum.FROSTBITE)
			if (!player.isSpectator() && !player.isCreative() && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
				player.setYBodyRot(player.getYRot() + (float) (Math.cos((double) player.tickCount * 3.25D) * Math.PI * (double) 0.4F));
			}
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
	
	public CompoundTag writeNBT() 
	{
		CompoundTag compound = new CompoundTag();
		
		compound.putFloat("temperature", this.temperature);
		compound.putFloat("targettemperature", this.targetTemp);
		compound.putInt("ticktimer", this.temperatureTickTimer);
		
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
	}
}
