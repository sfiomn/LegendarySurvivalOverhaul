package icey.survivaloverhaul.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

// Code taken from the More Bars mod by Vemerion
// https://github.com/vemerion/More-Bars/blob/master/src/main/java/mod/vemerion/morebars/bar/Bar.java
@SuppressWarnings("unused")
public abstract class StatBase
{
	protected float value;
	
	public StatBase(float value)
	{
		
	}

	public abstract float minValue();
	public abstract float maxValue();
	
	public abstract String getName();
	
	// Returns the absolute range of a stat
	public float getAbsoluteRange() 
	{
		return maxValue() - minValue();
	}
	
	public float getValue() 
	{
		return value;
	}
}