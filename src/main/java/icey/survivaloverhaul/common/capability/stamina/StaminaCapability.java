package icey.survivaloverhaul.common.capability.stamina;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.stamina.PlayerState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;

public class StaminaCapability 
{
	// Constants
	public static final int BASE_STAMINA = 20;
	public static final int STAMINA_INCREMENT = BASE_STAMINA / 5;
	public static final int RECOVERY_DELAY = 10;
	public static final int REPLENISH_TIMER_LIMIT = 6;
	
	// Saved data
	private PlayerState state;
	private int stamina;
	private int maxStamina;
	private boolean isDepleted;
	private int replenishTimer;
	private int recoveryDelay;
	
	// Unsaved data
	private int packetTimer;
	private PlayerState prevState;
	private boolean prevIsClimbing;
	
	private boolean movementNeedsSync;
	private boolean positionNeedsSync;
	private boolean climbingNeedsSync;
	
	
	public StaminaCapability()
	{
		this.init();
	}
	
	public void init()
	{
		this.state = PlayerState.IDLE;
		this.stamina = BASE_STAMINA;
		this.maxStamina = BASE_STAMINA;
		this.isDepleted = false;
		this.replenishTimer = 0;
		this.recoveryDelay = 0;
		
		this.packetTimer = 0;
		this.prevState = this.state;
		this.prevIsClimbing = false;
		
		this.movementNeedsSync = false;
		this.positionNeedsSync = false;
		this.climbingNeedsSync = false;
	}
	
	public void tickUpdate(PlayerEntity player, World world, Phase phase)
	{
		if (phase == Phase.START)
		{
			packetTimer++;
			return;
		}
		
		updateStamina(player, world);
	}
	
	public void clientTickUpdate(PlayerEntity player, World world, Phase phase)
	{
		if (phase == Phase.START)
		{
			return;
		}
		
		updateStamina(player, world);
		
		if(!player.abilities.isCreativeMode && isDepleted())
		{
			player.setSprinting(false);
			player.setSwimming(false);
		}
	}
	
	protected void updateStamina(PlayerEntity player, World world)
	{
		if (state.action.consumed)
		{
			recoveryDelay = RECOVERY_DELAY;
			
			if (!isDepleted && state.isClimbing())
			{
				addStamina(-state.action.change);
			}
				
		}
		else
		{
			if (recoveryDelay > 0) 
				recoveryDelay--;
			else
			{
				replenishTimer++;
				if (replenishTimer >= REPLENISH_TIMER_LIMIT)
				{
					addStamina(state.action.change);
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private PlayerState updatePlayerState(PlayerEntity player, World world)
	{
		if (player.abilities.isFlying)
			return PlayerState.IDLE;
		else if (player.isSwimming())
			return PlayerState.SWIMMING;
		else if (!player.isOnGround() && !player.isElytraFlying())
		{
			BlockPos pos = player.getPosition();
			BlockPos posUp = pos.up();
			Direction facing = player.getHorizontalFacing();
		}
		
		if (player.isSprinting()) 
			return PlayerState.RUNNING;
		else if (player.isOnGround())
			return PlayerState.IDLE;
		else		
			return PlayerState.MIDAIR;
	}
	
	public boolean canClimb(PlayerEntity player)
	{
		return player.isCreative() || !this.isDepleted;
	}
	
	public int getPacketTimer()
	{
		return packetTimer;
	}
	
	public void setDepleted(boolean depleted)
	{
		this.isDepleted = depleted;
	}
	
	public boolean isDepleted()
	{
		return isDepleted;
	}
	
	public int getStamina()
	{
		return this.stamina;
	}
	
	public void setStamina(int stamina)
	{
		this.stamina = MathHelper.clamp(stamina, 0, this.maxStamina);
	}
	
	public void addStamina(int stamina)
	{
		this.setStamina(this.stamina + stamina);
	}
	
	public int getMaxStamina()
	{
		return this.maxStamina;
	}
	
	public void setMaxStamina(int maxStamina)
	{
		this.maxStamina = MathHelper.clamp(maxStamina, BASE_STAMINA, Integer.MAX_VALUE);
	}
	
	public void addMaxStamina(int maxStamina)
	{
		this.setMaxStamina(this.maxStamina + maxStamina);
	}
	
	public PlayerState getStaminaState()
	{
		return this.state;
	}
	
	public CompoundNBT save()
	{
		CompoundNBT compound = new CompoundNBT();
		
		return compound;
	}
	
	public void load(CompoundNBT compound)
	{
		this.init();
	}
	
	public static StaminaCapability getStaminaCapability(PlayerEntity player)
	{
		return player.getCapability(Main.STAMINA_CAP).orElse(new StaminaCapability());
	}
}
