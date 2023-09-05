package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IThirstUtil
{
	@Nullable
	public HydrationEnum traceWater(PlayerEntity player);

	public void takeDrink(PlayerEntity player, int thirst, float saturation, float dirtyChance);

	public void takeDrink(PlayerEntity player, int thirst, float saturation);

	public void takeDrink(PlayerEntity player, HydrationEnum type);

	public void addExhaustion(PlayerEntity player, float exhaustion);

	public HydrationEnum getHydrationEnumLookedAt(PlayerEntity player, double finalDistance);

	public void setThirstEnumTag(final ItemStack stack, HydrationEnum hydrationEnum);

	public HydrationEnum getHydrationEnumTag(final ItemStack stack);

	public void removeHydrationEnumTag(final ItemStack stack);

	public void setCapacityTag(final ItemStack stack, int capacity);

	public int getCapacityTag(final ItemStack stack);

	public void removeCapacityTag(final ItemStack stack);
}
