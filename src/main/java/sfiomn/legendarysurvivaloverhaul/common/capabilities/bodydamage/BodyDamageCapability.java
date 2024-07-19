package sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage;

import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import org.apache.commons.lang3.tuple.Triple;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.*;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.*;


public class BodyDamageCapability implements IBodyDamageCapability
{
	// Saved data
	private Map<BodyPartEnum, BodyPart> bodyParts;
	private int headacheTimer;

	// Unsaved data
	private int updateTickTimer; // Update immediately first time around
	private float playerMaxHealth;
	private boolean manualDirty;
	private int packetTimer;
	private List<Triple<MalusBodyPartEnum, MobEffect, Integer>> malus;

	public BodyDamageCapability()
	{
		this.init();
	}

	public void init()
	{
		this.updateTickTimer = 20;
		this.headacheTimer = 0;
		this.playerMaxHealth = 0;
		this.manualDirty = false;

		this.bodyParts = new HashMap<>();
		this.malus = new ArrayList<>();

		this.bodyParts.put(BodyPartEnum.HEAD, new BodyPart(BodyPartEnum.HEAD, (float) Config.Baked.headPartHealth));
		this.bodyParts.put(BodyPartEnum.RIGHT_ARM, new BodyPart(BodyPartEnum.RIGHT_ARM, (float) Config.Baked.armsPartHealth));
		this.bodyParts.put(BodyPartEnum.LEFT_ARM, new BodyPart(BodyPartEnum.LEFT_ARM, (float) Config.Baked.armsPartHealth));
		this.bodyParts.put(BodyPartEnum.CHEST, new BodyPart(BodyPartEnum.CHEST, (float) Config.Baked.chestPartHealth));
		this.bodyParts.put(BodyPartEnum.RIGHT_LEG, new BodyPart(BodyPartEnum.RIGHT_LEG, (float) Config.Baked.legsPartHealth));
		this.bodyParts.put(BodyPartEnum.RIGHT_FOOT, new BodyPart(BodyPartEnum.RIGHT_FOOT, (float) Config.Baked.feetPartHealth));
		this.bodyParts.put(BodyPartEnum.LEFT_LEG, new BodyPart(BodyPartEnum.LEFT_LEG, (float) Config.Baked.legsPartHealth));
		this.bodyParts.put(BodyPartEnum.LEFT_FOOT, new BodyPart(BodyPartEnum.LEFT_FOOT, (float) Config.Baked.feetPartHealth));

		if (!Objects.equals(Config.Baked.bodyPartHealthMode, "DYNAMIC")) {
			for (BodyPart part: this.bodyParts.values()) {
				part.setMaxHealth(part.getHealthMultiplier());
			}
		}
	}

	@Override
	public void setManualDirty() {
		this.manualDirty = true;
	}

	@Override
	public boolean isDirty() {
		for (BodyPart bodyPart: this.bodyParts.values()) {
			if (bodyPart.isDirty())
				return true;
		}
		return manualDirty;
	}

	@Override
	public void setClean() {
		for (BodyPart bodyPart: this.bodyParts.values()) {
			bodyPart.setClean();
		}
		this.manualDirty = false;
	}

	@Override
	public int getPacketTimer() {
		return this.packetTimer;
	}

	@Override
	public void tickUpdate(Player player, Level world, TickEvent.Phase phase)
	{
		if(phase == TickEvent.Phase.START) {
			this.packetTimer++;
			return;
		};

		if (updateTickTimer++ >= 20) {
			updateTickTimer = 0;
			if (Objects.equals(Config.Baked.bodyPartHealthMode, "DYNAMIC") && playerMaxHealth != player.getMaxHealth()) {
				playerMaxHealth = player.getMaxHealth();
				updateDynamicMaxHealth(playerMaxHealth);
			}

			// Refresh all the malus a player should have
			List<Triple<MalusBodyPartEnum, MobEffect, Integer>> newMalus = new ArrayList<>();
			for (MalusBodyPartEnum malusBodyPart: MalusBodyPartEnum.values()) {
				List<Pair<MobEffect, Integer>> malusEffects = new ArrayList<>();
				if (!player.hasEffect(MobEffectRegistry.PAINKILLER.get()))
					malusEffects = BodyDamageUtil.getEffects(malusBodyPart, getHealthRatioForMalusBodyPart(malusBodyPart));
				for (Triple<MalusBodyPartEnum, MobEffect, Integer> bodyPartMalusEffect: this.malus) {
					if (bodyPartMalusEffect.getLeft() == malusBodyPart) {
						Pair<MobEffect, Integer> oldEffect = Pair.of(bodyPartMalusEffect.getMiddle(), bodyPartMalusEffect.getRight());
						if (!malusEffects.contains(oldEffect)) {
							player.removeEffect(oldEffect.getFirst());
							if (oldEffect.getFirst() == MobEffectRegistry.HEADACHE.get())
								player.removeEffect(MobEffects.BLINDNESS);
						}
					}
				}
				for (Pair<MobEffect, Integer> malusEffect: malusEffects) {
					newMalus.add(Triple.of(malusBodyPart, malusEffect.getFirst(), malusEffect.getSecond()));
				}
			}

			this.malus = newMalus;

			// Assign all malus effect to the player
			for (Triple<MalusBodyPartEnum, MobEffect, Integer> malusEffect: this.malus) {
				player.addEffect(new MobEffectInstance(malusEffect.getMiddle(), 300, malusEffect.getRight(), false, false, true));
			}

			// Heal each body limb of the player
			for (Map.Entry<BodyPartEnum, BodyPart> bodyPartPair: this.bodyParts.entrySet()) {
				BodyPart bodyPart = bodyPartPair.getValue();
				if (bodyPart.getRemainingHealingTicks() > 0) {
					int healingTick = Math.min(20, bodyPart.getRemainingHealingTicks());
					this.heal(bodyPartPair.getKey(), healingTick * bodyPart.getHealingPerTicks());
					if (bodyPart.isMaxHealth())
						bodyPart.reduceRemainingHealingTicks(bodyPart.getRemainingHealingTicks());
					else
						bodyPart.reduceRemainingHealingTicks(healingTick);
				}
			}
		}

		if (player.hasEffect(MobEffectRegistry.HEADACHE.get())) {
			if (this.headacheTimer-- < 0) {
				player.level().playSound(null ,player, SoundRegistry.HEADACHE_HEARTBEAT.get(), SoundSource.PLAYERS, 1.f, 1.0f);
				applyHeadache(player, Objects.requireNonNull(player.getEffect(MobEffectRegistry.HEADACHE.get())).getAmplifier());
			}
		} else {
			this.headacheTimer = 0;
		}
	}

	private void applyHeadache(Player player, int amplifier) {
		int blindnessTime = (40 + player.getRandom().nextInt(100)) * Math.min(amplifier + 1, 4);
		this.headacheTimer = blindnessTime + Math.round((float) (200 + player.getRandom().nextInt(400)) / (float) Math.min(amplifier + 1, 4));
		player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, blindnessTime, 0, false, false, true));
	}

	@Override
	public boolean isWounded() {
		for (BodyPart part: this.bodyParts.values()) {
			if (!part.isMaxHealth())
				return true;
		}
		return false;
	}

	@Override
	public float getBodyPartDamage(BodyPartEnum part) {
		return this.bodyParts.get(part).getDamage();
	}

	@Override
	public float getBodyPartMaxHealth(BodyPartEnum part) {
		return this.bodyParts.get(part).getMaxHealth();
	}

	@Override
	public void setBodyPartDamage(BodyPartEnum part, float damageValue) {
		this.bodyParts.get(part).setDamage(damageValue);
	}

	@Override
	public void setBodyPartMaxHealth(BodyPartEnum part, float maxHealthValue) {
		this.bodyParts.get(part).setMaxHealth(maxHealthValue);
	}

	@Override
	public void heal(BodyPartEnum part, float healingValue) {
		this.bodyParts.get(part).heal(healingValue);
	}

	@Override
	public void hurt(BodyPartEnum part, float damageValue) {
		this.bodyParts.get(part).hurt(damageValue);
	}

	@Override
	public void applyHealingItem(BodyPartEnum part, int healingTicks, float healingPerTick) {
		this.bodyParts.get(part).setHealing(healingTicks, healingPerTick);
	}

	@Override
	public float getBodyPartHealthRatio(BodyPartEnum part) {
		BodyPart bodyPart = this.bodyParts.get(part);
		return MathUtil.round((bodyPart.getMaxHealth() - bodyPart.getDamage()) / bodyPart.getMaxHealth(), 2);
	}

	@Override
	public float getRemainingHealingTicks(BodyPartEnum part) {
		return this.bodyParts.get(part).getRemainingHealingTicks();
	}

	@Override
	public float getHealingPerTicks(BodyPartEnum part) {
		return this.bodyParts.get(part).getHealingPerTicks();
	}

	@Override
	public float getHealthRatioForMalusBodyPart(MalusBodyPartEnum part) {
		switch (part) {
			case HEAD:
				return this.getBodyPartHealthRatio(BodyPartEnum.HEAD);
			case ARMS:
				return Math.min(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_ARM), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_ARM));
			case BOTH_ARMS:
				return Math.max(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_ARM), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_ARM));
			case CHEST:
				return this.getBodyPartHealthRatio(BodyPartEnum.CHEST);
			case LEGS:
				return Math.min(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_LEG), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_LEG));
			case BOTH_LEGS:
				return Math.max(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_LEG), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_LEG));
			case FEET:
				return Math.min(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_FOOT), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_FOOT));
			case BOTH_FEET:
				return Math.max(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_FOOT), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_FOOT));
			default:
				return 0.0f;
		}
	}

	private void updateDynamicMaxHealth(float maxHealth) {
		for (BodyPart bodyPart: this.bodyParts.values()) {
			bodyPart.setMaxHealth(Math.round(bodyPart.getHealthMultiplier() * maxHealth * 100) / 100.0f);
		}
	}

	public CompoundTag writeNBT()
	{
		CompoundTag compound = new CompoundTag();
		for (BodyPart bodyPart: this.bodyParts.values()) {
			compound = bodyPart.writeNbt(compound);
		}
		compound.putInt("headacheTimer", this.headacheTimer);

		return compound;
	}

	public void readNBT(CompoundTag compound)
	{
		this.init();
		for (BodyPart bodyPart: this.bodyParts.values()) {
			bodyPart.readNBT(compound);
		}

		this.headacheTimer = compound.getInt("headacheTimer");
	}
}
