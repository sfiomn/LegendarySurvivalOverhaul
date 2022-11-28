package sfiomn.legendarysurvivaloverhaul.common.capability.heartmods;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.heartmod.IHeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.UUID;

public class HeartModifierCapability implements IHeartModifierCapability
{
	public static final UUID HEART_MODIFIER_ATTRIBUTE = UUID.fromString("b158dbba-c193-4301-9dfd-82c4347b2cf4");
	private int extraHearts;
	
	// Unsaved Data
	private int oldExtraHearts;
	private boolean manualDirty;
	private int packetTimer;
	
	public HeartModifierCapability()
	{
		this.init();
	}
	
	public void init()
	{
		extraHearts = 0;
		
		oldExtraHearts = 0;
		manualDirty = false;
		packetTimer = 0;
	}
	
	public void updateMaxHealth(World world, PlayerEntity player)
	{
		ModifiableAttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);

        AttributeModifier modifier = health.getModifier(HEART_MODIFIER_ATTRIBUTE);
        if (modifier != null) {
            health.removeModifier(modifier);
        }
        
        health.addPermanentModifier(new AttributeModifier(HEART_MODIFIER_ATTRIBUTE, LegendarySurvivalOverhaul.MOD_ID + ":extra_hearts", extraHearts * 2, AttributeModifier.Operation.ADDITION));
	}
	
	public void setMaxHealth(int extraHearts)
	{
		this.extraHearts = MathHelper.clamp(extraHearts, 0, Config.Baked.maxAdditionalHearts);
	}
	
	public void addMaxHealth(int extraHearts)
	{
		this.setMaxHealth(this.extraHearts + extraHearts);
	}
	
	@Override
	public int getAdditionalHearts()
	{
		return extraHearts;
	}

	@Override
	public boolean isDirty()
	{
		return extraHearts != oldExtraHearts || manualDirty;
	}

	@Override
	public void setClean()
	{
		oldExtraHearts = extraHearts;
	}

	@Override
	public int getPacketTimer()
	{
		return packetTimer;
	}
	
	public CompoundNBT writeNBT() 
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putInt("extraHearts", extraHearts);
		
		return compound;
	}
	
	/*
	 * Note that this does not update the player's maximum health attribute.
	 * It is expected that any member calling this function will immediately call
	 * HeartModifierCapability$updateMaxHealth afterwards.
	 */
	public void readNBT(CompoundNBT compound)
	{
		this.init();

		if (compound.contains("extraHearts"))
				this.setMaxHealth(compound.getInt("extraHearts"));
	}
}
