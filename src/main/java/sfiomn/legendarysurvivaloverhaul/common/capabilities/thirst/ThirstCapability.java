package sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.DamageSourceUtil;
import sfiomn.legendarysurvivaloverhaul.util.DamageUtil;


public class ThirstCapability implements IThirstCapability
{
	private float exhaustion = 0.0f;
	private int thirst;
	private float thirstSaturation;
	private int thirstTickTimer;
	private int damageTickTimer; // Update immediately first time around
	private int damageCounter;

	//Unsaved data
	private int oldThirst;
	private float oldThirstSaturation;
	private boolean wasSprinting;
	private Vec3 oldPos;
	private boolean dirty;
	private int packetTimer;

	public ThirstCapability()
	{
		this.init();
	}

	public void init()
	{
		this.thirst = 20;
		this.thirstSaturation = 5.0f;
		this.thirstTickTimer = 0;
		this.damageCounter = 0;

		this.oldThirst = 0;
		this.oldThirstSaturation = 0.0f;
		this.wasSprinting = false;
		this.oldPos = null;
		this.dirty = false;
		this.damageTickTimer = 0;
		this.packetTimer = 0;
	}

	@Override
	public void tickUpdate(Player player, Level level, TickEvent.Phase phase)
	{
		if (getThirstTickTimer() == -1)
			return;

		if(phase == TickEvent.Phase.START)
		{
			packetTimer++;
			return;
		}

		if (oldPos == null)
			oldPos = player.position();

		this.addThirstTickTimer(1);
		if(getThirstTickTimer() >= 10)
		{
			this.setThirstTickTimer(0);

			// if player has moved at least 1 block, trigger the thirst exhaust, allowing afk player not dying from thirst
			if (oldPos.distanceTo(player.position()) > 1) {
				float thirstExhausted;
				if (player.isSprinting() && this.wasSprinting)
					thirstExhausted = (float) Config.Baked.sprintingThirstExhaustion;
				else if (player.isSprinting() || this.wasSprinting)
					thirstExhausted = (float) ((Config.Baked.sprintingThirstExhaustion + Config.Baked.baseThirstExhaustion) / 2.0d);
				else
					thirstExhausted = (float) Config.Baked.baseThirstExhaustion;

				this.addThirstExhaustion(thirstExhausted);
				this.oldPos = player.position();
				this.wasSprinting = player.isSprinting();
			}
		}

		// Process exhaustion, similar to hunger system. At 4 exhaustion, remove 1 thirst level or thirst saturation
		if(this.getThirstExhaustion() > 4)
		{
			// Exhausted, do a thirst tick
			this.addThirstExhaustion(-4.0f);

			if(this.getSaturationLevel() > 0.0f)
			{
				// Exhaust from saturation
				this.addSaturationLevel(-1.0f);
			}
			else if(DamageUtil.isModDangerous(level))
			{
				// Exhaust from thirst
				this.addHydrationLevel(-1);
			}
		}

		// Hurt ticking
		if(this.getHydrationLevel() <= 0 )
		{
			this.addThirstDamageTickTimer(1);

			// Hurt player every 4s, similar as hunger hurting
			if (this.getThirstDamageTickTimer() > 80)
			{
				this.setThirstDamageTickTimer(0);

				if(DamageUtil.isModDangerous(level) &&
						DamageUtil.healthAboveDifficulty(level, player) &&
						!player.isSpectator() && !player.isCreative() &&
						Config.Baked.dangerousDehydration)
				{
					applyDangerousEffect(player);
				}
			}
		}
		else
		{
			//Reset the timer if not dying of thirst
			this.setThirstDamageTickTimer(0);
			this.setThirstDamageCounter(0);
		}
	}

	private void applyDangerousEffect(Player player) {
		// Apply dehydration damages
		this.addThirstDamageCounter(1);
		float thirstDamageToApply = (float) (this.getThirstDamageCounter() * Config.Baked.dehydrationDamageScaling);
		player.hurt(DamageSourceUtil.getDamageSource(player.level(), ModDamageTypes.DEHYDRATION), thirstDamageToApply);
	}

	@Override
	public boolean isDirty()
	{
		return this.thirst!=this.oldThirst ||
				this.thirstSaturation !=this.oldThirstSaturation ||
				this.dirty;
	}

	@Override
	public void setClean()
	{
		this.oldThirst =this.thirst;
		this.oldThirstSaturation =this.thirstSaturation;
		this.dirty = false;
	}

	@Override
	public void setDirty() {
		this.dirty = true;
	}

	@Override
	public float getThirstExhaustion()
	{
		return exhaustion;
	}

	@Override
	public int getHydrationLevel()
	{
		return thirst;
	}

	@Override
	public float getSaturationLevel()
	{
		return thirstSaturation;
	}

	@Override
	public int getThirstTickTimer()
	{
		return thirstTickTimer;
	}

	@Override
	public int getThirstDamageTickTimer()
	{
		return damageTickTimer;
	}

	@Override
	public int getThirstDamageCounter()
	{
		return damageCounter;
	}

	@Override
	public void setThirstExhaustion(float exhaustion)
	{
		this.exhaustion=Math.max(exhaustion,0.0f);

		if(!Float.isFinite(this.exhaustion))
			this.exhaustion = 0.0f;
	}

	@Override
	public void setHydrationLevel(int thirst)
	{
		this.thirst = Mth.clamp(thirst, 0, 20);

	}

	@Override
	public void setThirstSaturation(float saturation)
	{
		this.thirstSaturation = Mth.clamp(saturation, 0.0f, 20.0f);

		if(!Float.isFinite(this.thirstSaturation))
			this.thirstSaturation = 0.0f;
	}

	@Override
	public void setThirstTickTimer(int ticktimer)
	{
		this.thirstTickTimer = ticktimer;
	}

	@Override
	public void setThirstDamageTickTimer(int damageTickTimer)
	{
		this.damageTickTimer = damageTickTimer;
	}

	@Override
	public void setThirstDamageCounter(int damageCounter)
	{
		this.damageCounter = damageCounter;
	}

	@Override
	public void addThirstExhaustion(float exhaustion)
	{
		this.setThirstExhaustion(this.getThirstExhaustion() + exhaustion);
	}

	@Override
	public void addHydrationLevel(int thirst)
	{
		this.setHydrationLevel(this.getHydrationLevel() + thirst);
	}

	@Override
	public void addSaturationLevel(float saturation)
	{
		// Never allow thirst saturation lower than 0
		this.setThirstSaturation(Math.max(Math.round((this.getSaturationLevel() + saturation) * 100.0f) / 100.0f, 0.0f));
	}

	@Override
	public void addThirstTickTimer(int ticktimer)
	{
		this.setThirstTickTimer(this.getThirstTickTimer() + ticktimer);
	}

	@Override
	public void addThirstDamageTickTimer(int damageTickTimer)
	{
		this.setThirstDamageTickTimer(this.getThirstDamageTickTimer() + damageTickTimer);
	}

	@Override
	public void addThirstDamageCounter(int damageCounter)
	{
		this.setThirstDamageCounter(this.getThirstDamageCounter() + damageCounter);
	}

	@Override
	public boolean isHydrationLevelAtMax()
	{
		return this.getHydrationLevel() >= 20;
	}

	@Override
	public int getPacketTimer()
	{
		return packetTimer;
	}

	public CompoundTag writeNBT()
	{
		CompoundTag compound = new CompoundTag();
		compound.putFloat("thirstExhaustion", this.getThirstExhaustion());
		compound.putInt("thirstLevel", this.getHydrationLevel());
		compound.putFloat("thirstSaturation", this.getSaturationLevel());
		compound.putInt("thirstTickTimer", this.getThirstTickTimer());
		compound.putInt("thirstDamageTickTimer", this.getThirstDamageTickTimer());
		compound.putInt("thirstDamageCounter", this.getThirstDamageCounter());
		return compound;
	}

	public void readNBT(CompoundTag nbt)
	{
		this.init();
		if(nbt.contains("thirstExhaustion"))
			this.setThirstExhaustion(nbt.getFloat("thirstExhaustion"));
		if(nbt.contains("thirstLevel"))
			this.setHydrationLevel(nbt.getInt("thirstLevel"));
		if(nbt.contains("thirstSaturation"))
			this.setThirstSaturation(nbt.getFloat("thirstSaturation"));
		if(nbt.contains("thirstTickTimer"))
			this.setThirstTickTimer(nbt.getInt("thirstTickTimer"));
		if(nbt.contains("thirstDamageTickTimer"))
			this.setThirstDamageTickTimer(nbt.getInt("thirstDamageTickTimer"));
		if(nbt.contains("thirstDamageCounter"))
			this.setThirstDamageCounter(nbt.getInt("thirstDamageCounter"));
	}
}
