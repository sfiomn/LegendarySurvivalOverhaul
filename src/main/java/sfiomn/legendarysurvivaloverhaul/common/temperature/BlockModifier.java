package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonPropertyTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureTileEntity;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.util.SpreadPoint;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sfiomn.legendarysurvivaloverhaul.util.MathUtil.addToAverage;

public class BlockModifier extends ModifierBase
{
	private final int tempInfluenceMaximumDist = Config.Baked.tempInfluenceMaximumDist;
	private float coldestValue = 0.0f;
	private float hottestValue = 0.0f;
	private float hotTotal = 0.0f;
	private float coldTotal = 0.0f;

	private int tickCount = 0;
	private double maxProcessTime = 0;
	private float averageProcessTime = 0;

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
		doTileEntitiesRoutine(world, pos);
		
		hotTotal -= hottestValue;
		coldTotal -= coldestValue;
		
		float hotLogValue = hottestValue * (float)Math.sqrt(easyLog(hotTotal));
		float coldLogValue = coldestValue * (float)Math.sqrt(easyLog(coldTotal));
		
		float result = hotLogValue + coldLogValue;

		if(result > hottestValue)
		{
			//Hotter than hottestValue, clamp
			return Math.min(hottestValue + 2.5f, result);
		}
		else if(result < coldestValue)
		{
			//Colder than coldestValue, clamp
			return Math.max(coldestValue - 2.5f, result);
		}
		else
		{
			//Within bounds, no need to clamp
			return result;
		}
	}
	
	private void doBlocksAndFluidsRoutine(World world, BlockPos pos)
	{
		// We don't use a MutableBoundingBox since it's easier to conceptualize

		HashMap<BlockPos, SpreadPoint> spreadBlockPos = new HashMap<>();
		ArrayList<SpreadPoint> spreadPointToProcess = new ArrayList<>();

		// TODO : remove perf logs when done with updating mod
		float start = 0;
		if (false) {
			start = (float) System.nanoTime() / 1000000;
		}

		SpreadPoint spreadPointFeetPlayer = new SpreadPoint(pos, null, tempInfluenceMaximumDist - 1, 0, new ArrayList<>(), world);
		spreadPointToProcess.add(spreadPointFeetPlayer);
		spreadBlockPos.put(spreadPointFeetPlayer.position(), spreadPointFeetPlayer);

		while (!spreadPointToProcess.isEmpty()) {
			SpreadPoint spreadPoint = spreadPointToProcess.remove(0);
			spreadPoint.setCanSeeSky();

			for (Direction direction : Direction.values()) {

				//  Avoid spreading to the direction we are coming from
				if (spreadPoint.originalDirection() == null || direction != spreadPoint.originalDirection().getOpposite()) {
					BlockPos newBlockPos = spreadPoint.newSpreadPos(direction);

					//  Check that the new spread location isn't an already processed location or is an already processed location but closer to the player
					if (!spreadBlockPos.containsKey(newBlockPos)) {

						SpreadPoint newSpreadPoint = spreadPoint.spreadTo(direction);
						spreadBlockPos.put(newSpreadPoint.position(), newSpreadPoint);

						//  If it is a valid spreadPoint (= AIR or not colliding block), store the spreadPoint as to be processed
						if (newSpreadPoint.isValidSpreadPoint()) {
							spreadPointToProcess.add(newSpreadPoint);
						}
					}
				}
			}
		}

		for (SpreadPoint spreadPoint : spreadBlockPos.values()) {
			processTemp(getTemperatureFromSpreadPoint(world, spreadPoint));
		}

		// TODO : remove perf logs when done with updating mod
		if (false) {
			float end = (float) System.nanoTime() / 1000000;
			averageProcessTime += addToAverage(averageProcessTime, tickCount, end - start);
			tickCount++;
			if (maxProcessTime < (end - start)) {
				maxProcessTime = end - start;
			}
			if (tickCount % 100 == 0) {
				LegendarySurvivalOverhaul.LOGGER.debug("Average Elapsed Time in ms: " + averageProcessTime + " for " + tickCount + " ticks");
				LegendarySurvivalOverhaul.LOGGER.debug("Max Elapsed Time in ms: " + maxProcessTime);
				LegendarySurvivalOverhaul.LOGGER.debug("CanSeeSky average process time " + SpreadPoint.averageProcessTimeCanSeeSkyCheck + " for " + SpreadPoint.numberCanSeeSkyCheck + " checks");
				LegendarySurvivalOverhaul.LOGGER.debug("NoCollide block average process time " + SpreadPoint.averageProcessTimeNoCollideBlockCheck + " for " + SpreadPoint.numberNoCollideBlockCheck + " checks");
				LegendarySurvivalOverhaul.LOGGER.debug("Empty block average process time " + SpreadPoint.averageProcessTimeEmptyBlockCheck + " for " + SpreadPoint.numberEmptyBlockCheck + " checks");
				tickCount = 0;
				averageProcessTime = 0;
				SpreadPoint.resetCounters();
			}
		}
	}
	
	private void doTileEntitiesRoutine(World world, BlockPos pos)
	{
		for (int x = -3; x <= 3; x++)
		{
			for (int z = -3; z <= 3; z++)
			{
				checkChunkAndProcess(world, pos.offset(x * 16, 0, z * 16), pos);
			}
		}
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
	
	private void checkChunkAndProcess(World world, BlockPos pos, BlockPos selfPos)
	{
		try
		{
			if (WorldUtil.isChunkLoaded(world, pos))
			{
				Chunk chunk = world.getChunkSource().getChunk(pos.getX() >> 4, pos.getZ() >> 4, false);
				
				for (Map.Entry<BlockPos, TileEntity> entry : chunk.getBlockEntities().entrySet())
				{
					processTemp(checkTileEntity(world, entry.getKey(), entry.getValue(), selfPos));
				}
			}
		}
		catch (Exception e)
		{
			
		}
	}
	
	private float checkTileEntity(World world, BlockPos pos, TileEntity tileEntity, BlockPos selfPos)
	{
		double distance = pos.distSqr(selfPos);
		if (distance < 2500.0d)
		{
			// Within 50 blocks
			
			if (tileEntity instanceof ITemperatureTileEntity)
			{
				return ((ITemperatureTileEntity) tileEntity).getInfluence(selfPos, distance);
			}
		}
		
		return 0.0f;
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

	private float temperatureAfterDistanceInfluence(float tempIn, SpreadPoint spreadPoint) {
		float distanceBeforeDecrease = 2.0f;

		// Have the max temperature gain outside if we are at 2 blocks of the temperature source
		if (spreadPoint.influenceDistance() <= distanceBeforeDecrease) {
			return tempIn;
		} else {
			float normalizedSpreadCost = (float) ((tempInfluenceMaximumDist - spreadPoint.spreadCapacity()) / spreadPoint.influenceDistance());
			return (float) (tempIn * (spreadPoint.spreadCapacity() / (tempInfluenceMaximumDist - distanceBeforeDecrease * normalizedSpreadCost)));
		}
	}
}