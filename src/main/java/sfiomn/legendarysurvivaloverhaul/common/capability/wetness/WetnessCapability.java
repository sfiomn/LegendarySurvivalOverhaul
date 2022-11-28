package sfiomn.legendarysurvivaloverhaul.common.capability.wetness;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class WetnessCapability
{
	public static final int WETNESS_LIMIT = 400;
	
	private int wetness;
	private int packetTimer;
	
	private int oldWetness;
	
	public WetnessCapability()
	{
		this.init();
	}
	
	public void init()
	{
		this.wetness = 0;
		this.packetTimer = 0;
		
		this.oldWetness = this.wetness;
	}
	
	public int getWetness()
	{
		return this.wetness;
	}
	
	public void setWetness(int wetness)
	{
		this.wetness = MathHelper.clamp(wetness, 0, WETNESS_LIMIT);
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
	public void tickUpdate(PlayerEntity player, World world, Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}
		
		BlockPos pos = player.blockPosition();
		
		// If the player is in a boat, shift the position used for calculations up by one block
		// This way, sitting in a boat that's floating on the water won't increase a player's wetness
		if (player.getVehicle() instanceof BoatEntity && !player.getVehicle().hasImpulse)
		{
			pos = pos.above();
		}
		
		if (this.wetness > 0 && (world.getFluidState(pos).isEmpty() || world.getFluidState(pos.above()).isEmpty()))
			worldParticles(player, world);
		
		// Only tick wetness every 4 ticks
		if (world.getLevelData().getGameTime() % 4 != 0)
			return;
		
		if (player.getRemainingFireTicks() > 0 && !player.fireImmune())
			this.addWetness(-10);
		
		if (world.isRainingAt(pos))
			this.addWetness(5);
		else if (world.getFluidState(pos) != null)
		{
			FluidState fluidState = world.getFluidState(pos);
			
			// If the fluid is empty, we can just be like 
			// "alright cool" and ditch it
			if (fluidState.isEmpty())
			{
				this.addWetness(-3);
				return;
			}
			
			Fluid fluid = fluidState.getType();
			
			float fractionalLevel = MathUtil.invLerp(1, 8, fluidState.getAmount());
			
			if (((float) player.position().y()) > ((float) pos.getY()) + fractionalLevel + 0.0625f)
				return;
			
			// If/Else chains are frowned upon, i know, but just bear with me please
			if (fluid instanceof ForgeFlowingFluid)
			{
				ForgeFlowingFluid forgeFluid = (ForgeFlowingFluid) fluidState.getType();
				
				if (forgeFluid.getAttributes().isGaseous())
				{
					this.addWetness(-3);
					return;
				}
				
				int temperature = forgeFluid.getAttributes().getTemperature();
				
				if (temperature < 400)
				{
					this.addWetness(temperature / 100);
				}
				else
				{
					this.addWetness(-(temperature / 100));
				}
			}
			else if (fluid instanceof LavaFluid)
			{
				this.addWetness(-Math.round(40.0f * fractionalLevel));
			}
			else if (fluid instanceof WaterFluid)
			{
				this.addWetness(Math.round(8.0f * fractionalLevel));
			}
			else
			{
				// Last fallback, just assume that it's the same as water and go from there
				this.addWetness(Math.round(8.0f * fractionalLevel));
			}
		}
	}
	
	private void worldParticles(PlayerEntity player, World world)
	{
		Vector3d pos = player.position();
		AxisAlignedBB box = player.getBoundingBox();
		
		int particleSpawnRate = Math.round((1.0f - MathUtil.invLerp(0, WETNESS_LIMIT, this.wetness)) * 10f);
		
		if (particleSpawnRate == 0 || world.getLevelData().getGameTime() % particleSpawnRate == 0)
			((ServerWorld) world).sendParticles(ParticleTypes.FALLING_WATER, pos.x, pos.y + (box.getYsize()/2), pos.z, 1, box.getXsize()/3, box.getYsize()/4,box.getZsize()/3, 0);
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
	
	public CompoundNBT writeNBT()
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putInt("wetness", this.wetness);
		
		return compound;
	}
	
	public void readNBT(CompoundNBT compound)
	{
		this.init();
		
		if (compound.contains("wetness"))
			this.wetness = compound.getInt("wetness");
	}
	
	public static class Provider implements ICapabilitySerializable<INBT>
	{
		private LazyOptional<WetnessCapability> instance = LazyOptional.of(LegendarySurvivalOverhaul.WETNESS_CAP::getDefaultInstance);
		
		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
		{
			return LegendarySurvivalOverhaul.WETNESS_CAP.orEmpty(cap, instance);
		}

		@Override
		public INBT serializeNBT()
		{
			return LegendarySurvivalOverhaul.WETNESS_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.WETNESS_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
		}

		@Override
		public void deserializeNBT(INBT nbt)
		{
			LegendarySurvivalOverhaul.WETNESS_CAP.getStorage().readNBT(LegendarySurvivalOverhaul.WETNESS_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
		}
		
	}
	
	public static class Storage implements IStorage<WetnessCapability>
	{
		@Override
		public INBT writeNBT(Capability<WetnessCapability> capability, WetnessCapability instance, Direction side)
		{
			return instance.writeNBT();
		}

		@Override
		public void readNBT(Capability<WetnessCapability> capability, WetnessCapability instance, Direction side, INBT nbt)
		{
			if (nbt instanceof CompoundNBT)
			{
				instance.readNBT((CompoundNBT) nbt);
			}
		}
	}
}
