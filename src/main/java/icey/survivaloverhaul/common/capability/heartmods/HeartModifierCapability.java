package icey.survivaloverhaul.common.capability.heartmods;

import java.util.UUID;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.heartmod.IHeartModifierCapability;
import icey.survivaloverhaul.config.Config;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import net.minecraftforge.event.TickEvent.Phase;

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
            if (modifier.getAmount() == extraHearts) return;

            health.removeModifier(modifier);
        }
        
        health.applyPersistentModifier(new AttributeModifier(HEART_MODIFIER_ATTRIBUTE, Main.MOD_ID + ":extra_hearts", extraHearts * 2, AttributeModifier.Operation.ADDITION));
	}
	
	public void setMaxHealth(int extraHearts)
	{
		this.extraHearts = MathHelper.clamp(extraHearts, 0, Config.BakedConfigValues.maxAdditionalHearts);
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
	
	public CompoundNBT save() 
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putInt("extraHearts", extraHearts);
		
		return compound;
	}
	
	/*
	 * Note that this does not update the player's maximum health attribute.
	 * It is expected that any member calling this function will immediately call
	 * this.updateMaxHealth afterwards.
	 */
	public void load(CompoundNBT compound)
	{
		this.init();

		if (compound.contains("extraHearts"))
				this.setMaxHealth(compound.getInt("extraHearts"));
	}
	
	public static HeartModifierCapability getHeartModCapability(PlayerEntity player)
	{
		return player.getCapability(Main.HEART_MOD_CAP).orElse(new HeartModifierCapability());
	}
}
