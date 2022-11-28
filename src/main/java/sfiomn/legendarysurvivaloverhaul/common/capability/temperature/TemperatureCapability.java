package sfiomn.legendarysurvivaloverhaul.common.capability.temperature;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifier;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;

import java.util.HashMap;
import java.util.Map;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureCapability.java

public class TemperatureCapability implements ITemperatureCapability
{
	private int temperature;
	private int temperatureTickTimer;
	private Map<String, TemporaryModifier> temporaryModifiers;
	
	//Unsaved data
	private int oldTemperature;
	private int updateTimer; //Update immediately first time around
	private int targetTemp;
	private boolean manualDirty;
	private int oldModifierSize;
	private int packetTimer;
	
	public TemperatureCapability() 
	{
		this.init();
	}
	
	public void init()
	{
		this.temperature = TemperatureEnum.NORMAL.getMiddle();
		this.temperatureTickTimer = 0;
		
		this.temporaryModifiers = new HashMap<String, TemporaryModifier>();
		
		this.oldTemperature = 0;
		this.targetTemp = 0;
		this.manualDirty = false;
		this.oldModifierSize = 0;
		this.packetTimer = 0;
	}
	
	@Override
	public int getTemperatureLevel()
	{
		return temperature;
	}

	@Override
	public int getTemperatureTickTimer()
	{
		return temperatureTickTimer;
	}

	@Override
	public ImmutableMap<String, TemporaryModifier> getTemporaryModifiers()
	{
		return ImmutableMap.copyOf(temporaryModifiers);
	}

	@Override
	public void setTemperatureLevel(int temperature)
	{
		this.temperature = TemperatureUtil.clampTemperature(temperature);
	}

	@Override
	public void setTemperatureTickTimer(int tickTimer)
	{
		this.temperatureTickTimer = tickTimer;
	}


	@Override
	public void setTemporaryModifier(String name, float temp, int duration)
	{
		if (temp == 0.0f || !Float.isFinite(temp))
		{
			return;
		}
		
		if (this.getTemporaryModifiers().containsKey(name))
		{
			this.manualDirty = true;
		}
		
		this.temporaryModifiers.put(name, new TemporaryModifier(temp, duration));
	}

	@Override
	public void addTemperatureLevel(int temperature)
	{
		this.setTemperatureLevel(getTemperatureLevel() + temperature);
	}

	@Override
	public void addTemperatureTickTimer(int tickTimer)
	{
		this.setTemperatureTickTimer(this.temperatureTickTimer + tickTimer);
	}

	@Override
	public void clearTemporaryModifiers()
	{
		this.temporaryModifiers.clear();
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

			TemperatureEnum tempEnum = getTemperatureEnum();

			int destinationTemp = TemperatureUtil.clampTemperature(targetTemp);

			if (getTemperatureLevel() != destinationTemp) {
				tickTemperature(getTemperatureLevel(), destinationTemp);
			}

			if (player.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() == Items.DEBUG_STICK)
				LegendarySurvivalOverhaul.LOGGER.info(tempEnum + ", " + getTemperatureLevel() + " -> " + destinationTemp);

			if (Config.Baked.dangerousTemperature) {
				applyDangerousEffects(player, tempEnum);
			}
		}
		updateTemporaryModifiers();
	}

	private void applyDangerousEffects(PlayerEntity player, TemperatureEnum tempEnum) {
		if (tempEnum == TemperatureEnum.HEAT_STROKE) {
			if (TemperatureEnum.HEAT_STROKE.getMiddle() < getTemperatureLevel() && !player.isSpectator() && !player.isCreative() && !playerIsImmuneToHeat(player)) {
				// Apply hyperthermia
				player.removeEffect(EffectRegistry.HEAT_STROKE.get());
				player.addEffect(new EffectInstance(EffectRegistry.HEAT_STROKE.get(), 300, 0, false, true));
				return;
			}
		} else if (tempEnum == TemperatureEnum.FROSTBITE) {
			if (TemperatureEnum.FROSTBITE.getMiddle() >= getTemperatureLevel() && !player.isSpectator() && !player.isCreative() && !player.hasEffect(EffectRegistry.COLD_RESISTANCE.get())) {
				// Apply hypothermia
				player.removeEffect(EffectRegistry.FROSTBITE.get());
				player.addEffect(new EffectInstance(EffectRegistry.FROSTBITE.get(), 300, 0, false, true));
				return;
			}
		}
		player.removeEffect(EffectRegistry.HEAT_STROKE.get());
		player.removeEffect(EffectRegistry.FROSTBITE.get());
	}
	
	private void updateTemporaryModifiers()
	{
		Map<String, TemporaryModifier> tweaks = new HashMap<String, TemporaryModifier>();
		
		for(Map.Entry<String, TemporaryModifier> entry : temporaryModifiers.entrySet())
		{
			TemporaryModifier tm = entry.getValue();
			
			if (tm.duration > 0)
			{
				tweaks.put(entry.getKey(), new TemporaryModifier(tm.temperature, tm.duration -1));
			}
		}
		
		temporaryModifiers.clear();
		temporaryModifiers.putAll(tweaks);
		tweaks.clear();
		
		if (oldModifierSize != temporaryModifiers.size())
		{
			this.manualDirty = true;
		}
		
		oldModifierSize = temporaryModifiers.size();
	}
	
	private boolean playerIsImmuneToHeat(PlayerEntity player)
	{
		return player.hasEffect(EffectRegistry.HEAT_RESISTANCE.get()) || player.hasEffect(Effects.FIRE_RESISTANCE);
	}
	
	private void tickTemperature(int currentTemp, int destination)
	{
		int diff = Math.abs(destination - currentTemp);
		
		int tickTowards = 1;
		
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
	
	private int getTemperatureTickLimit()
	{
		int tickMax = Config.Baked.maxTickRate;
		int tickMin = Config.Baked.minTickRate;
		
		int tickrange = tickMax - tickMin;
		
		int tempRange = TemperatureEnum.HEAT_STROKE.getUpperBound() - TemperatureEnum.FROSTBITE.getLowerBound();
		
		int currentrange = Math.abs(getTemperatureLevel() - targetTemp);
		
		return Math.max(tickMin, tickMax - ((currentrange * tickrange) / tempRange));
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
		
		compound.putInt("temperature", this.temperature);
		compound.putInt("ticktimer", this.temperatureTickTimer);
		
		CompoundNBT modifiers = new CompoundNBT();
		
		for (String entry : temporaryModifiers.keySet())
		{
			CompoundNBT savedMod = new CompoundNBT();
			TemporaryModifier modifier = temporaryModifiers.get(entry);
			savedMod.putFloat("temperature", modifier.temperature);
			savedMod.putInt("duration", modifier.duration);
			
			modifiers.put(entry, savedMod);
		}
		
		compound.put("temporaryModifiers", modifiers);
		
		return compound;
	}
	
	public void readNBT(CompoundNBT compound)
	{
		this.init();
		if (compound.contains("temperature"))
			this.setTemperatureLevel(compound.getInt("temperature"));
		if (compound.contains("tickTimer"))
			this.setTemperatureTickTimer(compound.getInt("tickTimer"));
		
		
		if(compound.contains("temporaryModifiers"))
		{
			this.clearTemporaryModifiers();
			
			CompoundNBT modifiers = (CompoundNBT) compound.get("temporaryModifiers");
			
			for (String entry : modifiers.getAllKeys())
			{
				float modTemp = ((CompoundNBT) modifiers.get(entry)).getFloat("temperature");
				int modDuration = ((CompoundNBT) modifiers.get(entry)).getInt("duration");
				
				this.setTemporaryModifier(entry, modTemp, modDuration);
			}
		}
	}
}
