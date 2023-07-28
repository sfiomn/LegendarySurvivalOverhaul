package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class WorldUtil
{
	private WorldUtil() {}
	
	public static BlockPos getSidedBlockPos(World world, Entity entity)
	{
		if(!world.isClientSide)
		{
			return entity.blockPosition();
		}
		
		if(entity instanceof PlayerEntity)
		{
			return new BlockPos(entity.position().add(0, 0.5d, 0));
		}
		else if(entity instanceof ItemFrameEntity)
		{
			return new BlockPos(entity.position().add(0, -0.45d, 0));
		}
		else
		{
			return entity.blockPosition();
		}
	}
	
	public static boolean isChunkLoaded(World world, BlockPos pos)
	{
		if(world.isClientSide)
		{
			return true;
		}
		else
		{
			return ((ServerWorld) world).getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4);
		}
	}

	public static ResourceLocation getBiomeName(World world, Biome biome) {
		if (world.registryAccess().registry(Registry.BIOME_REGISTRY).isPresent()) {
			return world.registryAccess().registry(Registry.BIOME_REGISTRY).get().getKey(biome);
		}
		return null;
	}

	public static boolean isRainingOrSnowingAt(World world, BlockPos pos) {
		if (!world.isRaining()) {
			return false;
		} else if (!world.canSeeSky(pos)) {
			return false;
		} else return world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, pos).getY() <= pos.getY();
	}
	
	public static float calculateClientWorldEntityTemperature(World world, Entity entity)
	{
		return TemperatureUtil.getWorldTemperature(world, getSidedBlockPos(world, entity));
	}

	public static Entity getEntityLookedAt(PlayerEntity player, double finalDistance) {
		Entity foundEntity = null;
		double distance;
		RayTraceResult positionLookedAt = player.pick(finalDistance, 0.0f, false);
		Vector3d positionVector = player.position();

		positionVector = positionVector.add(0, player.getEyeHeight(player.getPose()), 0);

		distance = positionLookedAt.getLocation().distanceTo(positionVector);

		Vector3d lookVector = player.getLookAngle();
		Vector3d reachVector = positionVector.add(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance);

		Entity lookedEntity = null;
		List<Entity> entitiesInBoundingBox = player.getCommandSenderWorld().getEntities(player, player.getBoundingBox().inflate(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance).expandTowards(1F, 1F, 1F));
		double minDistance = distance;

		for (Entity entity : entitiesInBoundingBox) {
			if (entity.isPickable()) {
				AxisAlignedBB collisionBox = entity.getBoundingBoxForCulling();
				Optional<Vector3d> interceptPosition = collisionBox.clip(positionVector, reachVector);

				if (collisionBox.contains(positionVector)) {
					if (minDistance >= 0.0D) {
						lookedEntity = entity;
						minDistance = 0.0D;
					}
				} else if (interceptPosition.isPresent()) {
					double distanceToEntity = positionVector.distanceTo(interceptPosition.get());

					if (minDistance > distanceToEntity || minDistance == 0.0D) {
						lookedEntity = entity;
						minDistance = distanceToEntity;
					}
				}
			}

			if (lookedEntity != null && minDistance < distance)
				foundEntity = lookedEntity;
		}

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

	public static Set<Vector3i> getAllDirectionsVectors() {
		Set<Vector3i> allDirectionsVectors = new HashSet<>();
		for (Direction direction : Direction.values()) {
			allDirectionsVectors.add(direction.getNormal());
			for (Direction direction1 : Direction.values()) {
				if (direction1.getAxis() != direction.getAxis()) {
					Vector3i newDirection = direction.getNormal().relative(direction1, 1);
					allDirectionsVectors.add(newDirection);
					for (Direction direction2 : Direction.values()) {
						if (direction2.getAxis() != direction1.getAxis() && direction2.getAxis() != direction.getAxis()) {
							Vector3i newDirection1 = newDirection.relative(direction2, 1);
							allDirectionsVectors.add(newDirection1);
						}
					}
				}
			}
		}
		return allDirectionsVectors;
	}

	public static Vector3i getOppositeVector(Vector3i originalVector) {
		return new Vector3i(-originalVector.getX(), -originalVector.getY(), -originalVector.getZ());
	}
}
