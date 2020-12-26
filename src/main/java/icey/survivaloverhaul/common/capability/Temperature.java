package icey.survivaloverhaul.common.capability;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import icey.survivaloverhaul.api.temperature.ITemperatureCapability;
import icey.survivaloverhaul.api.temperature.TemporaryModifier;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureCapability.java

@SuppressWarnings("unused")
public class Temperature implements ITemperatureCapability
{
	private int temperature;
	private int tickTimer;
	private Map<String, TemporaryModifier> temporaryModifiers;
	
	//Unsaved data
	private int oldTemperature;
	private int updateTimer; //Update immediately first time around
	private int targetTemp;
	private int debugTimer;
	private boolean manualDirty;
	private int oldModifierSize;
	private int packetTimer;
	
	public Temperature() 
	{
		this.init();
	}
	
	public void init()
	{
		this.temperature = TemperatureEnum.NORMAL.getMiddle();
		this.tickTimer = 0;
		
		this.temporaryModifiers = new HashMap<String, TemporaryModifier>();
		
		this.oldTemperature = 0;
		this.targetTemp = 0;
		this.debugTimer = 0;
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
		if(updateTimer >= 5)
		{
			updateTimer = 0;
			
			targetTemp = TemperatureUtil.getPlayerTargetTemperature(player);
		}
		
		addTemperatureTickTimer(1);
		
		if (getTemperatureTickTimer() >= getTemperatureTickLimit())
		{
			setTemperatureTickTimer(0);
			
			int destinationTemp = TemperatureUtil.clampTemperature(targetTemp);
			
			if (getTemperatureLevel() != destinationTemp)
			{
				if (getTemperatureLevel() > destinationTemp)
				{
					addTemperatureLevel(-1);
				}
				else 
				{
					addTemperatureLevel(1);
				}
			}
			
			TemperatureEnum tempEnum = getTemperatureEnum();
			if(tempEnum == TemperatureEnum.HYPERTHERMIA)
			{
				if(TemperatureEnum.HYPERTHERMIA.getMiddle() < getTemperatureLevel())
				{
					// This is where we'd apply the hyperthermia effect, but since that hasn't been implemented yet,
					// we'll print to the log that we're supposed to be freezing now.
					
				}
			}
			else if (tempEnum == TemperatureEnum.HYPOTHERMIA)
			{

				if(TemperatureEnum.HYPOTHERMIA.getMiddle() < getTemperatureLevel())
				{
					// This is where we'd apply the hypothermia effect, but since that hasn't been implemented yet,
					// we'll print to the log that we're supposed to be overheating now.
				}
			}
		}
	}
	
	private int getTemperatureTickLimit()
	{
		int tickMax = 200;
		int tickMin = 25;
		
		int tickrange = tickMax - tickMin;
		
		int tempRange = TemperatureEnum.HYPERTHERMIA.getUpperBound() - TemperatureEnum.HYPOTHERMIA.getLowerBound();
		
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
		
		this.temperature = compound.getInt("temperature");
		this.tickTimer = compound.getInt("tickTimer");
		
		CompoundNBT modifiers = (CompoundNBT) compound.get("temporaryModifiers");
		
		for (String entry : modifiers.keySet())
		{
			float modTemp = ((CompoundNBT) modifiers.get(entry)).getFloat("temperature");
			int modDuration = ((CompoundNBT) modifiers.get(entry)).getInt("duration");
			
			TemporaryModifier newMod = new TemporaryModifier(modTemp, modDuration);
		}
	}
	
	
}
