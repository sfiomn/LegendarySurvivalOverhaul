package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

import java.util.HashSet;
import java.util.Set;

public final class WorldUtil
{
	private WorldUtil() {}
	
	public static BlockPos getSidedBlockPos(Level world, Entity entity)
	{
		if(!world.isClientSide)
		{
			return entity.blockPosition();
		}
		
		if(entity instanceof Player)
		{
			return BlockPos.containing(entity.position().add(0, 0.5d, 0));
		}
		else if(entity instanceof ItemFrame)
		{
			return BlockPos.containing(entity.position().add(0, -0.45d, 0));
		}
		else
		{
			return entity.blockPosition();
		}
	}
	
	public static boolean isChunkLoaded(Level world, BlockPos pos)
	{
		if(world.isClientSide)
		{
			return true;
		}
		else
		{
			return world.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4);
		}
	}

	public static ResourceLocation getBiomeName(Level world, Biome biome) {
		if (world.registryAccess().registry(Registries.BIOME).isPresent()) {
			return world.registryAccess().registry(Registries.BIOME).get().getKey(biome);
		}
		return null;
	}

	public static boolean isRainingOrSnowingAt(Level world, BlockPos pos) {
		if (!world.isRaining()) {
			return false;
		} else if (!world.canSeeSky(pos)) {
			return false;
		} else return world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() <= pos.getY();
	}
	
	public static float calculateClientWorldEntityTemperature(Level world, Entity entity)
	{
		return TemperatureUtil.getWorldTemperature(world, getSidedBlockPos(world, entity));
	}

	public static Entity getEntityLookedAt(Player player, double finalDistance) {
		Entity foundEntity = null;
		double distanceFromEye;
		HitResult positionLookedAt = player.pick(finalDistance, 0.0f, false);
		Vec3 eyePosition = player.getEyePosition(0.0f);

		distanceFromEye = positionLookedAt.getLocation().distanceTo(eyePosition);

		Vec3 lookVector = player.getLookAngle();
		Vec3 reachVector = eyePosition.add(lookVector.x * distanceFromEye, lookVector.y * distanceFromEye, lookVector.z * distanceFromEye);

		AABB expandedPlayerBound = player.getBoundingBox().expandTowards(lookVector.x * distanceFromEye, lookVector.y * distanceFromEye, lookVector.z * distanceFromEye);

		EntityHitResult entityRayTraceResult = ProjectileUtil.getEntityHitResult(player, eyePosition, reachVector, expandedPlayerBound, (entity) -> !entity.isSpectator() && entity.isPickable(), distanceFromEye * distanceFromEye);
		if (entityRayTraceResult != null) {
			foundEntity = entityRayTraceResult.getEntity();
		}

		// Here for the understanding of ProjectileHelper
		/*
		Entity lookedEntity = null;
		List<Entity> entitiesInBoundingBox = player.level.getEntities(player, expandedPlayerBound, (entity) -> !entity.isSpectator() && entity.isPickable());

		double minDistance = distanceFromEye;

		for (Entity entity : entitiesInBoundingBox) {
			AxisAlignedBB collisionBox = entity.getBoundingBoxForCulling();
			Optional<Vector3d> interceptPosition = collisionBox.clip(eyePosition, reachVector);

			if (collisionBox.contains(eyePosition)) {
				if (minDistance >= 0.0D) {
					lookedEntity = entity;
					minDistance = 0.0D;
				}
			} else if (interceptPosition.isPresent()) {
				double distanceToEntity = eyePosition.distanceTo(interceptPosition.get());
				if (entity.getRootVehicle() == player.getRootVehicle() && !entity.canRiderInteract()) {
					if (minDistance == 0.0D) {
						lookedEntity = entity;
					}
				} else if (minDistance > distanceToEntity || minDistance == 0.0D) {
					lookedEntity = entity;
					minDistance = distanceToEntity;
				}
			}

			if (lookedEntity != null && minDistance < distanceFromEye)
				foundEntity = lookedEntity;
		}*/

		return foundEntity;
	}

	public static String timeInGame(Minecraft mc) {
		int dayCount = 0;
		int dayTime = mc.level != null ? (int) mc.level.dayTime() : 0;
		while (dayTime > 24000) {
			dayTime -= 24000;
			dayCount++;
		}

		dayTime = (dayTime + 6000) % 24000;

		String hourOfTheDay = String.format("%2s", dayTime / 1000).replace(' ', '0');
		String minuteOfTheDay = String.format("%2s", (dayTime % 1000) * 6 / 100).replace(' ', '0');

		return "Day " + dayCount + ", " + hourOfTheDay + ":" + minuteOfTheDay;
	}

	public static Vec3i getOppositeVector(Vec3i originalVector) {
		return new Vec3i(-originalVector.getX(), -originalVector.getY(), -originalVector.getZ());
	}
}
