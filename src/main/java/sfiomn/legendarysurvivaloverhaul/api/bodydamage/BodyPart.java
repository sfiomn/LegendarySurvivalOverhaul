package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

public class BodyPart {
    // Unsaved Data
    private float oldMaxHealth;
    private float oldDamage;
    private final float healthMultiplier;
    private final BodyPartEnum bodyPartEnum;

    // Saved Data
    private float damage;
    private float maxHealth;
    private int remainingHealingTicks;
    private float healingPerTicks;

    public BodyPart(BodyPartEnum bodyPart, float healthMultiplier) {
        this.bodyPartEnum = bodyPart;
        this.healthMultiplier = healthMultiplier;
        this.oldMaxHealth = 0;
        this.oldDamage = 0;
    }

    public boolean isDirty() {
        return this.oldDamage != this.damage || this.oldMaxHealth != this.maxHealth;
    }

    public void setClean() {
        this.oldDamage = this.damage;
        this.oldMaxHealth = this.maxHealth;
    }

    public void heal(float value) {
        this.setDamage(this.damage - value);
    }

    public void hurt(float value) {
        this.setDamage(this.damage + value);
    }

    public boolean isMaxHealth() {
        return this.damage == 0;
    }

    public float getHealthMultiplier() {
        return this.healthMultiplier;
    }

    public BodyPartEnum getBodyPartEnum() {
        return this.bodyPartEnum;
    }

    public void setDamage(float value) {
        this.damage = MathHelper.clamp(value, 0, this.maxHealth);
    }

    public float getDamage() {
        return this.damage;
    }

    public void setMaxHealth(float value) {
        this.maxHealth = value;
        this.damage = Math.min(this.maxHealth, this.damage);
    }

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public int getRemainingHealingTicks() {
        return this.remainingHealingTicks;
    }

    public void reduceRemainingHealingTicks(int healingTick) {
        this.remainingHealingTicks -= Math.min(healingTick, this.remainingHealingTicks);
        if (this.remainingHealingTicks == 0) {
            this.healingPerTicks = 0;
        }
    }

    public float getHealingPerTicks() {
        return this.healingPerTicks;
    }

    public void setHealing(int healingTick, float healingValuePerTick) {
        this.remainingHealingTicks = healingTick;
        this.healingPerTicks = healingValuePerTick;
    }

    public CompoundNBT writeNbt(CompoundNBT nbt) {
        nbt.putFloat(this.bodyPartEnum.name() + "_damage", this.damage);
        nbt.putFloat(this.bodyPartEnum.name() + "_maxHealth", this.maxHealth);
        nbt.putFloat(this.bodyPartEnum.name() + "_healingPerTicks", this.healingPerTicks);
        nbt.putInt(this.bodyPartEnum.name() + "_remainingHealingTicks", this.remainingHealingTicks);
        return nbt;
    }

    public void readNBT(CompoundNBT compound) {
        this.setMaxHealth(compound.getFloat(this.bodyPartEnum.name() + "_maxHealth"));
        this.setDamage(compound.getFloat(this.bodyPartEnum.name() + "_damage"));
        this.remainingHealingTicks = compound.getInt(this.bodyPartEnum.name() + "_remainingHealingTicks");
        this.healingPerTicks = compound.getFloat(this.bodyPartEnum.name() + "_healingPerTicks");
    }
}
