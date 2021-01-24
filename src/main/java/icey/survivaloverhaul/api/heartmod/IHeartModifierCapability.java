package icey.survivaloverhaul.api.heartmod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;

public interface IHeartModifierCapability
{
	public int getAdditionalHearts();
	
	/**
	 * (Don't use this!) <br>
	 * Checks if the capability needs an update
	 * @return boolean has temperature changed
	 */
	public boolean isDirty();
	/**
	 * (Don't use this!) <br>
	 * Sets the capability as updated
	 */
	public void setClean();
	
	/**
	 * (Don't use this!) <br>
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();
}
