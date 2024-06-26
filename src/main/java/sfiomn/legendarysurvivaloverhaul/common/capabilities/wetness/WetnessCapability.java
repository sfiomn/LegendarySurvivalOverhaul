package sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

public class WetnessCapability
{
	public static final int WETNESS_LIMIT = 400;
	
	private int wetness;
	private int packetTimer;
	private int updateTimer; //Update immediately first time around
	
	private int oldWetness;
	
	public WetnessCapability()
	{
		this.init();
	}
	
	public void init()
	{
		this.wetness = 0;
		this.packetTimer = 0;
		this.updateTimer = 0;
		
		this.oldWetness = this.wetness;
	}
	
	public int getWetness()
	{
		return this.wetness;
	}
	
	public void setWetness(int wetness)
	{
		this.wetness = Mth.clamp(wetness, 0, WETNESS_LIMIT);
	}
	
	public void addWetness(int wetness)
	{
		this.setWetness(this.wetness + wetness);
	}
	
	/**
	 * This probably isn't too terribly performance friendly but it's something at least<br>
	 * <br>
	 * TODO: optimization!!
	 */
	public void tickUpdate(Player player, Level level, Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}

		updateTimer++;
		if(updateTimer < 4)
		{
			return;
		}
		updateTimer = 0;

		if (this.wetness > 0 && player.getRemainingFireTicks() > 0 && !player.fireImmune())
			this.addWetness(-10);
		
		BlockPos pos = player.blockPosition();
		
		// If the player is in a boat, shift the position used for calculations up by one block
		// This way, sitting in a boat that's floating on the water won't increase a player's wetness
		if (player.getVehicle() instanceof Boat && !player.getVehicle().hasImpulse)
		{
			pos = pos.above();
			if (this.wetness > 0 && level.getFluidState(pos).isEmpty())
				worldParticles(player, level);
		} else {
			if (this.wetness > 0 && (level.getFluidState(pos).isEmpty() || level.getFluidState(pos.above()).isEmpty()))
				worldParticles(player, level);
		}

		FluidState fluidState = level.getFluidState(pos);
		FluidState fluidStateUp = level.getFluidState(pos.above());

		// If no fluid on the pos of the player (or above if in a boat)
		// only check for raining on pos above player (to avoid issue with half blocks)
		if (fluidState.isEmpty()) {
			if (wetness < WETNESS_LIMIT && level.isRainingAt(player.blockPosition().above()))
				this.addWetness(Config.Baked.wetnessRainIncrease);
			else if (this.wetness > 0)
				this.addWetness(Config.Baked.wetnessDecrease);
		}
		else if (!fluidState.isEmpty()) {
			Fluid fluid = fluidState.getType();
			
			float fractionalLevel = MathUtil.invLerp(1, 8, fluidState.getAmount());

			// if player is out of water
			if (((float) player.position().y()) > ((float) pos.getY()) + fractionalLevel + 0.0625f)
				return;

			// add the amount of fluid in the upper block as well
			if (!fluidStateUp.isEmpty())
				fractionalLevel += MathUtil.invLerp(1, 8, fluidStateUp.getAmount());

			if (fluid instanceof ForgeFlowingFluid)
			{
				ForgeFlowingFluid forgeFluid = (ForgeFlowingFluid) fluidState.getType();
				
				if (this.wetness > 0 && forgeFluid.getFluidType().isAir())				{
					this.addWetness(Config.Baked.wetnessDecrease);
					return;
				}
				
				int temperature = forgeFluid.getFluidType().getTemperature();
				
				if (this.wetness < WETNESS_LIMIT && temperature < 400)
				{
					this.addWetness(Math.round(Config.Baked.wetnessFluidIncrease * fractionalLevel));
				}
				else if (this.wetness > 0)
				{
					this.addWetness(Config.Baked.wetnessDecrease);
				}
			} else if (this.wetness > 0 && fluid instanceof LavaFluid) {
				this.addWetness(-Math.round(20.0f * fractionalLevel));
			}
			// Last fallback, just assume that it's the same as water and go from there
			else if (this.wetness < WETNESS_LIMIT)
			{
				this.addWetness(Math.round(Config.Baked.wetnessFluidIncrease * fractionalLevel));
			}
		}
	}
	
	private void worldParticles(Player player, Level level)
	{
		Vec3 pos = player.position();
		AABB box = player.getBoundingBox();
		
		int particleSpawnRate = Math.round((1.0f - MathUtil.invLerp(0, WETNESS_LIMIT, this.wetness)) * 10f);
		
		if (particleSpawnRate == 0 || level.getLevelData().getGameTime() % particleSpawnRate == 0)
			((ServerLevel) level).sendParticles(ParticleTypes.FALLING_WATER, pos.x, pos.y + (box.getYsize()/2), pos.z, 1, box.getXsize()/3, box.getYsize()/4,box.getZsize()/3, 0);
	}
	
	public boolean isDirty()
	{
		return this.wetness != this.oldWetness;
	}
	
	public void setClean()
	{
		this.oldWetness = this.wetness;
	}
	
	public int getPacketTimer()
	{
		return this.packetTimer;
	}
	
	public CompoundTag writeNBT()
	{
		CompoundTag compound = new CompoundTag();
		
		compound.putInt("wetness", this.wetness);
		
		return compound;
	}
	
	public void readNBT(CompoundTag compound)
	{
		this.init();
		
		if (compound.contains("wetness"))
			this.wetness = compound.getInt("wetness");
	}
}
