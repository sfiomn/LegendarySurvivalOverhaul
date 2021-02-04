package icey.survivaloverhaul.common.capability.temperature;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ITemperatureCapability;
import icey.survivaloverhaul.api.temperature.TemporaryModifier;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.registry.EffectRegistry;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureCapability.java

public class TemperatureCapability implements ITemperatureCapability
{
	public static final int FIRE_TIMER_LIMIT = 600;
	public static final int WETNESS_LIMIT = 400;
	
	private int temperature;
	private int tickTimer;
	private Map<String, TemporaryModifier> temporaryModifiers;
	
	private int wetness;
	private int fireTimer;
	
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
		this.tickTimer = 0;
		this.wetness = 0;
		this.fireTimer = 0;
		
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
		return tickTimer;
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
	public void setTemperatureTickTimer(int ticktimer)
	{
		this.tickTimer = ticktimer;
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
	public void addTemperatureTickTimer(int ticktimer)
	{
		this.setTemperatureTickTimer(this.tickTimer + ticktimer);
	}

	@Override
	public int getWetness()
	{
		return this.wetness;
	}

	@Override
	public void setWetness(int wetness)
	{
		this.wetness = MathHelper.clamp(wetness, 0, WETNESS_LIMIT);
	}

	@Override
	public void addWetness(int wetness)
	{
		this.setWetness(this.wetness + wetness);
	}

	@Override
	public int getFireTimer()
	{
		return this.fireTimer;
	}

	@Override
	public void setFireTimer(int fireTimer)
	{
		this.fireTimer = MathHelper.clamp(fireTimer, 0, FIRE_TIMER_LIMIT);
	}

	@Override
	public void addFireTimer(int fireTimer)
	{
		this.setFireTimer(this.fireTimer + fireTimer);
	}

	@Override
	public void clearTemporaryModifiers()
	{
		this.temporaryModifiers.clear();
	}
	
	@Override
	public void tickTemperature(PlayerEntity player, World world, Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}
		
		updateTimer++;
		if(updateTimer >= 5)
		{
			updateTimer = 0;
			
			targetTemp = TemperatureUtil.getPlayerTargetTemperature(player);
		}
		
		addTemperatureTickTimer(1);
		
		tickWetness(player, world);
		tickFireTimer(player, world);
		
		if (getTemperatureTickTimer() >= getTemperatureTickLimit())
		{
			setTemperatureTickTimer(0);
			
			int destinationTemp = TemperatureUtil.clampTemperature(targetTemp);
			
			if (getTemperatureLevel() != destinationTemp)
			{
				tickTemperature(getTemperatureLevel(), destinationTemp);
			}
			
			TemperatureEnum tempEnum = getTemperatureEnum();
			
			if (player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == Items.DEBUG_STICK)
					Main.LOGGER.info(tempEnum + ", " + getTemperatureLevel() + " -> " + destinationTemp);
			
			if(tempEnum == TemperatureEnum.HEAT_STROKE)
			{
				if(TemperatureEnum.HEAT_STROKE.getMiddle() < getTemperatureLevel() && !player.isSpectator() && !player.isCreative() && !playerIsImmuneToHeat(player))
				{
					// Apply hyperthermia
					player.removePotionEffect(EffectRegistry.ModEffects.HEAT_STROKE);
					player.addPotionEffect(new EffectInstance(EffectRegistry.ModEffects.HEAT_STROKE, 300, 0, false, true));
				}
			}
			else if (tempEnum == TemperatureEnum.FROSTBITE)
			{
				if(TemperatureEnum.FROSTBITE.getMiddle() >= getTemperatureLevel() && !player.isSpectator() && !player.isCreative() && !player.isPotionActive(EffectRegistry.ModEffects.COLD_RESISTANCE))
				{
					// Apply hypothermia
					player.removePotionEffect(EffectRegistry.ModEffects.FROSTBITE);
					player.addPotionEffect(new EffectInstance(EffectRegistry.ModEffects.FROSTBITE, 300, 0, false, true));
				}
			}
		}
		
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
	
	private void tickWetness(PlayerEntity player, World world)
	{
		Fluid fluidIn = world.getFluidState(player.getPosition()).getFluid();
		Fluid fluidUp = world.getFluidState(player.getPosition().up()).getFluid();
		
		if (world.isRainingAt(player.getPosition()))
			addWetness(1);
		else if (fluidIn instanceof WaterFluid || fluidUp instanceof WaterFluid)
			addWetness(5);
		else
			addWetness(-2);
		
		if (player.getFireTimer() > 0 && this.getWetness() > 0)
		{
			addWetness(-8);
			player.forceFireTicks(player.getFireTimer() - 10);
		}
		
	}
	
	private void tickFireTimer(PlayerEntity player, World world)
	{
		
	}
	
	private boolean playerIsImmuneToHeat(PlayerEntity player)
	{
		return player.isPotionActive(EffectRegistry.ModEffects.HEAT_RESISTANCE) || player.isPotionActive(Effects.FIRE_RESISTANCE);
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
		this.oldTemperature = temperature;
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
	
	public CompoundNBT save() 
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putInt("temperature", this.temperature);
		compound.putInt("ticktimer", this.tickTimer);
		compound.putInt("wetness", this.wetness);
		compound.putInt("fireTimer", this.fireTimer);
		
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
	
	public void load(CompoundNBT compound)
	{
		this.init();
		if (compound.contains("temperature"))
			this.setTemperatureLevel(compound.getInt("temperature"));
		if (compound.contains("tickTimer"))
			this.setTemperatureTickTimer(compound.getInt("tickTimer"));
		if (compound.contains("wetness"))
			this.setFireTimer(compound.getInt("wetness"));
		if (compound.contains("fireTimer"))
			this.setFireTimer(compound.getInt("fireTimer"));
		
		
		if(compound.contains("temporaryModifiers"))
		{
			this.clearTemporaryModifiers();
			
			CompoundNBT modifiers = (CompoundNBT) compound.get("temporaryModifiers");
			
			for (String entry : modifiers.keySet())
			{
				float modTemp = ((CompoundNBT) modifiers.get(entry)).getFloat("temperature");
				int modDuration = ((CompoundNBT) modifiers.get(entry)).getInt("duration");
				
				this.setTemporaryModifier(entry, modTemp, modDuration);
			}
		}
	}
	
	public static TemperatureCapability getTempCapability(PlayerEntity player)
	{
		return player.getCapability(Main.TEMPERATURE_CAP).orElse(new TemperatureCapability());
	}
}
