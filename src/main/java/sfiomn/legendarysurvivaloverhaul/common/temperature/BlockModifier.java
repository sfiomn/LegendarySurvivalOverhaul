package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonBlockFluidTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import sfiomn.legendarysurvivaloverhaul.util.SpreadPoint;

import java.util.*;

import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.getOppositeVector;

public class BlockModifier extends ModifierBase
{
	private float coldestValue = 0.0f;
	private float hottestValue = 0.0f;
	private float hotTotal = 0.0f;
	private float coldTotal = 0.0f;

	public BlockModifier()
	{
		super();
	}

	public int tempInfluenceMaximumDist() {
		return Config.Baked.tempInfluenceMaximumDist - 1;
	}
	
	@Override
	public float getWorldInfluence(Level level, BlockPos pos)
	{
		coldestValue = 0.0f;
		hottestValue = 0.0f;
		
		hotTotal = 0.0f;
		coldTotal = 0.0f;
		
		doBlocksAndFluidsRoutine(level, pos.above());
		
		hotTotal -= hottestValue;
		coldTotal -= coldestValue;
		
		float hotLogValue = hottestValue * (float)Math.sqrt(easyLog(hotTotal));
		float coldLogValue = coldestValue * (float)Math.sqrt(easyLog(coldTotal));
		
		float result = hotLogValue + coldLogValue;

		if(result > hottestValue)
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("Block temp influence hotter than max : " + Math.min(hottestValue + 2.5f, result));
			// Hotter than hottestValue, clamp
			return Math.min(hottestValue + 2.5f, result);
		}
		else if(result < coldestValue)
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("Block temp influence hotter than max : " + Math.max(coldestValue - 2.5f, result));
			// Colder than coldestValue, clamp
			return Math.max(coldestValue - 2.5f, result);
		}
		else
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("Block temp influence : " + result);
			// Within bounds, no need to clamp
			return result;
		}
	}
	
	private void doBlocksAndFluidsRoutine(Level level, BlockPos pos)
	{
		HashMap<BlockPos, SpreadPoint> listVisitedBlockPos = new HashMap<>();
		ArrayList<SpreadPoint> spreadPointsToProcess = new ArrayList<>();

		SpreadPoint spreadPointHeadPlayer = new SpreadPoint(pos.above(), Direction.UP, tempInfluenceMaximumDist(), 0, level);
		SpreadPoint spreadPointFeetPlayer = new SpreadPoint(pos, Direction.DOWN, tempInfluenceMaximumDist(), 0, level);
		spreadPointsToProcess.add(spreadPointHeadPlayer);
		spreadPointsToProcess.add(spreadPointFeetPlayer);
		listVisitedBlockPos.put(spreadPointHeadPlayer.position(), spreadPointHeadPlayer);
		listVisitedBlockPos.put(spreadPointFeetPlayer.position(), spreadPointFeetPlayer);

		while (!spreadPointsToProcess.isEmpty()) {
			SpreadPoint spreadPoint = spreadPointsToProcess.remove(0);
			spreadPoint.setCanSeeSky();
			Direction oppositeDirection = spreadPoint.originalDirection().getOpposite();

			for (Direction direction : Direction.values()) {
				//  Avoid spreading to the direction we are coming from
				if (direction != oppositeDirection) {
					if (!spreadPoint.isValidSpreadDirection(direction))
						continue;
					Vec3i newPosVector = direction.getNormal();
					SpreadPoint spreadPoint1 = this.processDirectionTo(spreadPointsToProcess, listVisitedBlockPos, spreadPoint, spreadPoint.position().offset(newPosVector), direction, 1.0f);

					if (spreadPoint1 != null) {
						for (Direction direction1 : Direction.values()) {
							//  Check plan diagonal blocks
							if (direction1.getAxis() != direction.getAxis() && direction1 != oppositeDirection) {
								if (!spreadPoint1.isValidSpreadDirection(direction))
									continue;
								newPosVector = newPosVector.relative(direction1, 1);
								SpreadPoint spreadPoint2 = this.processDirectionTo(spreadPointsToProcess, listVisitedBlockPos, spreadPoint, spreadPoint.position().offset(newPosVector), direction1, 1.414f);

								if (spreadPoint2 != null) {
									for (Direction direction2 : Direction.values()) {
										//  Check 3D diagonal blocks
										if (direction2.getAxis() != direction1.getAxis() && direction2.getAxis() != direction.getAxis() && direction2 != oppositeDirection) {
											if (!spreadPoint2.isValidSpreadDirection(direction))
												continue;
											newPosVector = newPosVector.relative(direction2, 1);
											this.processDirectionTo(spreadPointsToProcess, listVisitedBlockPos, spreadPoint, spreadPoint.position().offset(newPosVector), direction2, 1.732f);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		for (SpreadPoint spreadPoint : listVisitedBlockPos.values()) {
			processTemp(getTemperatureFromSpreadPoint(level, spreadPoint));
		}
	}

	private SpreadPoint processDirectionTo(ArrayList<SpreadPoint> spreadPointsToProcess, HashMap<BlockPos, SpreadPoint> listVisitedBlockPos, SpreadPoint parentSpreadPoint, BlockPos newBlockPos, Direction originDirection, float distance) {
		//  Check that the new spread location isn't an already processed location
		if (!listVisitedBlockPos.containsKey(newBlockPos)) {

			SpreadPoint newSpreadPoint = parentSpreadPoint.spreadTo(newBlockPos, originDirection, distance);
			listVisitedBlockPos.put(newSpreadPoint.position(), newSpreadPoint);

			//  If it is a valid spreadPoint (= not colliding block), store the spreadPoint as to be processed
			if (newSpreadPoint.isValidSpreadPoint(originDirection)) {
				spreadPointsToProcess.add(newSpreadPoint);
				return newSpreadPoint;
			}
		} /*else {
			double newSpreadCapacity = spreadPoint.newSpreadCapacity(directionVector);
			if ((newSpreadCapacity - spreadBlockPos.get(newBlockPos).spreadCapacity()) > 0.2) {
				SpreadPoint newSpreadPoint = spreadPoint.spreadTo(directionVector);
				spreadBlockPos.put(newSpreadPoint.position(), newSpreadPoint);

				//  If it is a valid spreadPoint (= not colliding block), store the spreadPoint as to be processed
				if (newSpreadPoint.isValidSpreadPoint()) {
					spreadPointToProcess.removeIf(spreadPoint1 -> spreadPoint1.position().toShortString().equals(newSpreadPoint.position().toShortString()));
					spreadPointToProcess.add(newSpreadPoint);
					return true;
				}
			}
		}*/
		return null;
	}
	
	private void processTemp(float temp)
	{
		if (temp == 0.0f)
				return;
		
		if (temp >= 0.0f)
		{
			hotTotal += temp;
			if (temp > hottestValue)
			{
				hottestValue = temp;
			}
		}
		else
		{
			coldTotal += temp;
			if (temp < coldestValue)
			{
				coldestValue = temp;
			}
		}
	}
	
	private float easyLog(float f)
	{
		if(f >= 0.0f)
		{
			return (float)Math.log10(f + 10.0f);
		}
		else
		{
			return (float)Math.log10(-1.0f * f + 10.0f);
		}
	}

	private float getTemperatureFromSpreadPoint(Level level, SpreadPoint spreadPoint) {
		BlockState blockState = level.getBlockState(spreadPoint.position());
		float temperature = 0.0f;
		ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(blockState.getBlock());

		if (registryName == null || blockState.isAir()) {
			return 0.0f;
		}

		// List of combination of a temperature and a list of properties a block must have in order to generate this temperature
		List<JsonBlockFluidTemperature> tempPropertyList = JsonConfig.blockFluidTemperatures.get(registryName.toString());

		if (tempPropertyList == null) {
			return 0.0f;
		}

		for (JsonBlockFluidTemperature tempInfo : tempPropertyList) {
			if (tempInfo == null)
				continue;

			if (tempInfo.matchesState(blockState)) {
				temperature = tempInfo.temperature;
				break;
			}
		}

		return temperatureAfterDistanceInfluence(temperature, spreadPoint);
	}
/*
	private float temperatureAfterDistanceInfluence(float tempIn, SpreadPoint spreadPoint) {
		float distanceBeforeDecrease = 2.0f;

		// Have the max temperature gain outside if we are at 2 blocks of the temperature source
		if (spreadPoint.influenceDistance() <= distanceBeforeDecrease) {
			return tempIn;
		} else {
			//  Having how much spread capacity is consumed per distance to reach the point :
			//      max spread - spread capacity = spread consumed to reach the point
			//      divided by the distance to reach the point
			float normalizedSpreadCost = (float) ((tempInfluenceMaximumDist - spreadPoint.spreadCapacity()) / spreadPoint.influenceDistance());
			//  (1) dist before decrease * normalized spread cost = capacity consumed by the min dist that has max effect temp
			//  (2) spreadCapacity + (1) = add the capacity consumed by the min dist to the current capacity means that when we reach the border of the min
			//  dist, we are at the original capacity of the spread point.
			//  (2) / max capacity = % of capacity left vs max capacity.
			//      At max capacity it means that the min border of spread point is close to the player
			//      if there is almost not spread capacity left vs max capacity, it means the player is far from the min border
			return (float) (tempIn * (spreadPoint.spreadCapacity() + distanceBeforeDecrease * normalizedSpreadCost) / tempInfluenceMaximumDist);
		}
	}*/

	private float temperatureAfterDistanceInfluence(float tempIn, SpreadPoint spreadPoint) {
		// I use the sqrt(x) graph to convert how much the block temp should be decreased vs the distance to the block
		// [1] max spread - spread capacity = spread consumed to reach the point
		//     divided by max spread capacity to know the % of spread capacity consumed
		float capacityConsumed = MathUtil.round((float) (tempInfluenceMaximumDist() - spreadPoint.spreadCapacity()) / tempInfluenceMaximumDist(), 2);
		// [2] sqrt([1]) = % of temperature the block influence has lost
		// [3] (1 - [2]) * block temp = block influence based on distance of the player
		return MathUtil.round((float) Math.sqrt(1 - capacityConsumed) * tempIn, 2);
	}
}