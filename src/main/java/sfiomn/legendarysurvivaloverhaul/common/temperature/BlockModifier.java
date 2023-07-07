package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonPropertyTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.util.SpreadPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.getOppositeVector;

public class BlockModifier extends ModifierBase
{
	private final int tempInfluenceMaximumDist = Config.Baked.tempInfluenceMaximumDist;
	private float coldestValue = 0.0f;
	private float hottestValue = 0.0f;
	private float hotTotal = 0.0f;
	private float coldTotal = 0.0f;

	private static boolean once = true;

	public BlockModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		coldestValue = 0.0f;
		hottestValue = 0.0f;
		
		hotTotal = 0.0f;
		coldTotal = 0.0f;
		
		doBlocksAndFluidsRoutine(world, pos);
		
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
	
	private void doBlocksAndFluidsRoutine(World world, BlockPos pos)
	{
		HashMap<BlockPos, SpreadPoint> spreadBlockPos = new HashMap<>();
		ArrayList<SpreadPoint> spreadPointToProcess = new ArrayList<>();

		SpreadPoint spreadPointFeetPlayer = new SpreadPoint(pos, null, (tempInfluenceMaximumDist - 1), 0, world);
		spreadPointToProcess.add(spreadPointFeetPlayer);
		spreadBlockPos.put(spreadPointFeetPlayer.position(), spreadPointFeetPlayer);

		while (!spreadPointToProcess.isEmpty()) {
			SpreadPoint spreadPoint = spreadPointToProcess.remove(0);
			spreadPoint.setCanSeeSky();

			for (Direction direction : Direction.values()) {
				Vector3i directionVector = direction.getNormal();
				//  Avoid spreading to the direction we are coming from
				if (spreadPoint.originalDirection() == null || !Objects.equals(directionVector, getOppositeVector(spreadPoint.originalDirection()))) {
					boolean validSpreadPoint = this.processDirectionFrom(spreadPointToProcess, spreadBlockPos, spreadPoint, directionVector);

					if (validSpreadPoint) {
						for (Direction direction1 : Direction.values()) {
							//  Check plan diagonal blocks
							if (direction1.getAxis() != direction.getAxis()) {
								Vector3i directionVector1 = direction.getNormal().relative(direction1, 1);
								boolean validSpreadPoint1 = this.processDirectionFrom(spreadPointToProcess, spreadBlockPos, spreadPoint, directionVector1);
								if (validSpreadPoint1) {
									for (Direction direction2 : Direction.values()) {
										//  Check 3D diagonal blocks
										if (direction2.getAxis() != direction1.getAxis() && direction2.getAxis() != direction.getAxis()) {
											Vector3i directionVector2 = directionVector1.relative(direction2, 1);
											this.processDirectionFrom(spreadPointToProcess, spreadBlockPos, spreadPoint, directionVector2);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		for (SpreadPoint spreadPoint : spreadBlockPos.values()) {
			processTemp(getTemperatureFromSpreadPoint(world, spreadPoint));
		}
		once = false;
	}

	private boolean processDirectionFrom(ArrayList<SpreadPoint> spreadPointToProcess, HashMap<BlockPos, SpreadPoint> spreadBlockPos, SpreadPoint spreadPoint, Vector3i directionVector) {
		BlockPos newBlockPos = spreadPoint.newSpreadPos(directionVector);

		//  Check that the new spread location isn't an already processed location or is an already processed location but closer to the player
		if (!spreadBlockPos.containsKey(newBlockPos)) {

			SpreadPoint newSpreadPoint = spreadPoint.spreadTo(directionVector);
			spreadBlockPos.put(newSpreadPoint.position(), newSpreadPoint);

			//  If it is a valid spreadPoint (= not colliding block), store the spreadPoint as to be processed
			if (newSpreadPoint.isValidSpreadPoint()) {
				spreadPointToProcess.add(newSpreadPoint);
				return true;
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
		return false;
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

	private float getTemperatureFromSpreadPoint(World world, SpreadPoint spreadPoint) {
		BlockState blockState = world.getBlockState(spreadPoint.position());
		float temperature = 0.0f;

		if (blockState.getBlock().getRegistryName() == null) {
			return 0.0f;
		}

		if (blockState.getMaterial().isLiquid()) {
			//  Temperature a fluid generates
			JsonTemperature tempInfo = JsonConfig.fluidTemperatures.get(blockState.getBlock().getRegistryName().toString());

			if (tempInfo == null) {
				return 0.0f;
			}

			temperature = tempInfo.temperature;
		} else {

			//  List of combination of a temperature and a list of properties a block must have in order to generate this temperature
			List<JsonPropertyTemperature> tempInfoList = JsonConfig.blockTemperatures.get(blockState.getBlock().getRegistryName().toString());

			if (tempInfoList == null) {
				return 0.0f;
			}

			for (JsonPropertyTemperature tempInfo : tempInfoList) {
				if (tempInfo == null)
					continue;

				if (tempInfo.matchesState(blockState)) {
					temperature = tempInfo.temperature;
					break;
				}
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
		float capacityConsumed = (float) ((tempInfluenceMaximumDist - spreadPoint.spreadCapacity()) / tempInfluenceMaximumDist);
		// [2] sqrt([1]) = % of temperature the block influence has lost
		// [3] (1 - [2]) * block temp = block influence based on distance of the player
		return ((float) (1 - Math.sqrt(capacityConsumed))) * tempIn;
	}
}