package sfiomn.legendarysurvivaloverhaul.api.wetness;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonEffectParameter;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;

import java.util.List;

public interface IWetnessUtil
{
	public void addWetness(Player player, int wetness);

	public void deactivateWetness(Player player);

	public void activateWetness(Player player);

	public boolean isWetnessActive(Player player);
}
