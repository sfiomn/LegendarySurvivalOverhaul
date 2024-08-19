package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonEffectParameter;

import java.util.List;

public interface IThirstUtil
{
	@Nullable
	public HydrationEnum traceWater(Player player);

	public void takeDrink(Player player, int thirst, float saturation, List<JsonEffectParameter> effects);

	public void takeDrink(Player player, int thirst, float saturation);

	public void takeDrink(Player player, HydrationEnum type);

	public void addExhaustion(Player player, float exhaustion);

	public HydrationEnum getHydrationEnumLookedAt(Player player, double finalDistance);

	public void setThirstEnumTag(final ItemStack stack, HydrationEnum hydrationEnum);

	public HydrationEnum getHydrationEnumTag(final ItemStack stack);

	public void removeHydrationEnumTag(final ItemStack stack);

	public void setCapacityTag(final ItemStack stack, int capacity);

	public int getCapacityTag(final ItemStack stack);

	public void removeCapacityTag(final ItemStack stack);

	public JsonConsumableThirst getThirstJsonConfig(ResourceLocation itemRegistryName, ItemStack itemStack);

	public void deactivateThirst(Player player);

	public void activateThirst(Player player);

	public boolean isThirstActive(Player player);
}
