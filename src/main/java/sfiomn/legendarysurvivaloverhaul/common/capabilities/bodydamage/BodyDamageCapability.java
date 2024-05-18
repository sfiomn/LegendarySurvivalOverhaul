package sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;


public class BodyDamageCapability implements IBodyDamageCapability
{
	//Unsaved data
	private int updateTickTimer; // Update immediately first time around

	public BodyDamageCapability()
	{
		this.init();
	}

	public void init()
	{
		this.updateTickTimer = 0;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void setClean() {

	}

	public void tickUpdate(PlayerEntity player, World world, TickEvent.Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			return;
		}

		updateTickTimer++;
		if(updateTickTimer >= 10)
		{
			updateTickTimer = 0;
		}
	}

	@Override
	public int getPacketTimer() {
		return 0;
	}

	public CompoundNBT writeNBT()
	{
		CompoundNBT compound = new CompoundNBT();

		return compound;
	}

	public void readNBT(CompoundNBT compound)
	{
		this.init();
	}
}
