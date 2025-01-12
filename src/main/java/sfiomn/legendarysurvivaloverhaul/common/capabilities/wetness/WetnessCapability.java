package sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import sfiomn.legendarysurvivaloverhaul.api.wetness.IWetnessCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

public class WetnessCapability implements IWetnessCapability
{
	public static final int WETNESS_LIMIT = 400;
	
	private int wetness;
	private int wetnessTickTimer; //Update immediately first time around

	private int packetTimer;
	private int oldWetness;
	private boolean dirty = false;
	
	public WetnessCapability()
	{
		this.init();
	}
	
	public void init()
	{
		this.wetness = 0;
		this.wetnessTickTimer = 0;

		this.packetTimer = 0;
		this.oldWetness = this.wetness;
		this.dirty = false;
	}

	@Override
	public int getWetness()
	{
		return this.wetness;
	}

	@Override
	public int getWetnessTickTimer() {
		return this.wetnessTickTimer;
	}

	@Override
	public void setWetness(int wetness)
	{
		this.wetness = MathHelper.clamp(wetness, 0, WETNESS_LIMIT);
	}

	@Override
	public void setWetnessTickTimer(int tickTimer) {
		this.wetnessTickTimer = tickTimer;
	}

	@Override
	public void addWetness(int wetness)
	{
		this.setWetness(this.wetness + wetness);
	}

	@Override
	public void addWetnessTickTimer(int tickTimer) {
		this.setWetnessTickTimer(this.getWetnessTickTimer() + tickTimer);
	}
	
	/**
	 * This probably isn't too terribly performance friendly but it's something at least<br>
	 * <br>
	 * TODO: optimization!!
	 */
	@Override
	public void tickUpdate(PlayerEntity player, World world, Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}

		this.addWetnessTickTimer(1);
		if (this.getWetnessTickTimer() < Config.Baked.wetnessTickTimer)
			return;
		this.setWetnessTickTimer(0);

		if (this.wetness > 0 && player.getRemainingFireTicks() > 0 && !player.fireImmune())
			this.addWetness(-10);
		
		BlockPos pos = player.blockPosition();

		// If the player is not riding a living entity, shift the position used for calculations up by one block
		// This way, sitting in a boat that's floating on the water won't increase a player's wetness
		if (player.getVehicle() != null && !(player.getVehicle() instanceof LivingEntity) && !player.getVehicle().hasImpulse)
		{
			pos = pos.above();
			if (this.wetness > 0 && world.getFluidState(pos).isEmpty())
				worldParticles(player, world);
		} else {
			if (this.wetness > 0 && (world.getFluidState(pos).isEmpty() || world.getFluidState(pos.above()).isEmpty()))
				worldParticles(player, world);
		}

		FluidState fluidState = world.getFluidState(pos);
		BlockState blockState = world.getBlockState(pos);
		FluidState fluidStateUp = world.getFluidState(pos.above());

		// If no fluid on the pos of the player (or above if in a boat)
		// only check for raining on pos above player (to avoid issue with half blocks)
		if (fluidState.isEmpty() && !blockState.is(Blocks.CAULDRON)) {
			if (wetness < WETNESS_LIMIT && world.isRainingAt(player.blockPosition().above()))
				this.addWetness(Config.Baked.wetnessRainIncrease);
			else if (this.wetness > 0)
				this.addWetness(Config.Baked.wetnessDecrease);
		}
		else {
			Fluid fluid = Fluids.EMPTY;
			float fractionalLevel = 0.0f;

			if (!fluidState.isEmpty()) {
				fluid = fluidState.getType();
				fractionalLevel = MathUtil.invLerp(1, 8, fluidState.getAmount());
			} else if (blockState.is(Blocks.CAULDRON)) {
				fluid = Fluids.WATER;
				if (blockState.hasProperty(CauldronBlock.LEVEL))
					if (blockState.getValue(CauldronBlock.LEVEL) > 0) {
						fractionalLevel = MathUtil.invLerp(1, 3, blockState.getValue(CauldronBlock.LEVEL));
					} else {
						this.addWetness(Config.Baked.wetnessDecrease);
						return;
					}
			}

			// if player is out of water
			if (((float) player.position().y()) > ((float) pos.getY()) + fractionalLevel + 0.0625f)
				return;

			// add the amount of fluid in the upper block as well
			if (!fluidStateUp.isEmpty())
				fractionalLevel += MathUtil.invLerp(1, 8, fluidStateUp.getAmount());

			if (fluid instanceof ForgeFlowingFluid)
			{
				ForgeFlowingFluid forgeFluid = (ForgeFlowingFluid) fluidState.getType();

				if (this.wetness > 0 && forgeFluid.getAttributes().isGaseous())
				{
					this.addWetness(Config.Baked.wetnessDecrease);
					return;
				}

				int temperature = forgeFluid.getAttributes().getTemperature();

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
	
	private void worldParticles(PlayerEntity player, World world)
	{
		Vector3d pos = player.position();
		AxisAlignedBB box = player.getBoundingBox();
		
		int particleSpawnRate = Math.round((1.0f - MathUtil.invLerp(0, WETNESS_LIMIT, this.wetness)) * 10f);
		
		if (particleSpawnRate == 0 || world.getLevelData().getGameTime() % particleSpawnRate == 0)
			((ServerWorld) world).sendParticles(ParticleTypes.FALLING_WATER, pos.x, pos.y + (box.getYsize()/2), pos.z, 1, box.getXsize()/3, box.getYsize()/4,box.getZsize()/3, 0);
	}

	@Override
	public boolean isDirty()
	{
		return this.wetness != this.oldWetness;
	}

	@Override
	public void setClean()
	{
		this.oldWetness = this.wetness;
	}

	@Override
	public void setDirty() {
		this.dirty = true;
	}

	@Override
	public int getPacketTimer()
	{
		return this.packetTimer;
	}
	
	public CompoundNBT writeNBT()
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putInt("wetness", this.getWetness());
		compound.putInt("wetnessTickTimer", this.getWetnessTickTimer());
		
		return compound;
	}
	
	public void readNBT(CompoundNBT compound)
	{
		this.init();
		
		if (compound.contains("wetness"))
			this.setWetness(compound.getInt("wetness"));
		if (compound.contains("wetnessTickTimer"))
			this.setWetnessTickTimer(compound.getInt("wetnessTickTimer"));
	}
}
