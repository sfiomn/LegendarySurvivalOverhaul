package sfiomn.legendarysurvivaloverhaul.api.wetness;

import net.minecraft.entity.player.PlayerEntity;

public interface IWetnessUtil
{
	public void addWetness(PlayerEntity player, int wetness);

	public void deactivateWetness(PlayerEntity player);

	public void activateWetness(PlayerEntity player);

	public boolean isWetnessActive(PlayerEntity player);
}
