package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IThirstUtil
{
	@Nullable
	public ThirstEnum traceWater(PlayerEntity player);

	public void takeDrink(PlayerEntity player, int thirst, float saturation, float dirtyChance);

	public void takeDrink(PlayerEntity player, int thirst, float saturation);

	public void takeDrink(PlayerEntity player, ThirstEnum type);

	public void addExhaustion(PlayerEntity player, float exhaustion);

	public ThirstEnum getThirstEnumLookedAt(PlayerEntity player, double finalDistance);

	public ItemStack createPurifiedWaterBucket();

	public void setThirstEnumTag(final ItemStack stack, ThirstEnum thirstEnum);

	public ThirstEnum getThirstEnumTag(final ItemStack stack);

	public void removeThirstEnumTag(final ItemStack stack);

	public void setCapacityTag(final ItemStack stack, int capacity);

	public int getCapacityTag(final ItemStack stack);

	public void removeCapacityTag(final ItemStack stack);
}
