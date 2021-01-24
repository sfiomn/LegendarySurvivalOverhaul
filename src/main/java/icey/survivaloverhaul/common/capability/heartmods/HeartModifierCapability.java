package icey.survivaloverhaul.common.capability.heartmods;

import java.util.UUID;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.heartmod.IHeartModifierCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.config.Config;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import net.minecraftforge.event.TickEvent;
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

	@Override
	public void tickUpdate(PlayerEntity player, World world, Phase phase)
	{
		packetTimer++;
		
		ModifiableAttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
		
		AttributeModifier mod = health.getModifier(HEART_MODIFIER_ATTRIBUTE);
		
		if (mod != null)
		{
			if (mod.getAmount() == extraHearts * 2) return;
			
			health.removeModifier(mod);
		}
		
		health.applyPersistentModifier(new AttributeModifier(HEART_MODIFIER_ATTRIBUTE, Main.MOD_ID + ":extra_hearts", extraHearts * 2, AttributeModifier.Operation.ADDITION));
	}
	
	@Override
	public int getAdditionalHearts()
	{
		return extraHearts;
	}

	@Override
	public void setHearts(int hearts)
	{
		this.extraHearts = MathHelper.clamp(hearts, 0, Config.BakedConfigValues.maxAdditionalHearts);
	}

	@Override
	public void addHearts(int hearts)
	{
		this.setHearts(this.getAdditionalHearts() + hearts);
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
	
	public void load(CompoundNBT compound)
	{
		this.init();

		if (compound.contains("extraHearts"))
				this.setHearts(compound.getInt("extraHearts"));
	}
	
	public static HeartModifierCapability getHeartModCapability(PlayerEntity player)
	{
		return player.getCapability(Main.HEART_MOD_CAP).orElse(new HeartModifierCapability());
	}
}
