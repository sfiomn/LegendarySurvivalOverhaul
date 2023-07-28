package sfiomn.legendarysurvivaloverhaul.api.config.json.temperature;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

/**
 * Code taken and adapted from Charles445's SimpleDifficulty mod
 * @see <a href="https://github.com/Charles445/SimpleDifficulty/tree/master/src/main/java/com/charles445/simpledifficulty/api/config/json">Github Link</a>
 * @author Charles445
 * @author Icey
 */

public class JsonConsumableTemperature
{
	public TemporaryModifierGroupEnum group;
	public int temperatureLevel;
	public int duration;
	private RegistryObject<Effect> effect;
	private RegistryObject<Effect> oppositeEffect;
	
	public JsonConsumableTemperature(TemporaryModifierGroupEnum group, int temperatureLevel, int duration)
	{
		this.temperatureLevel = temperatureLevel;
		this.duration = duration;
		this.group = group;
		this.effect = null;
		this.oppositeEffect = null;
		if (temperatureLevel > 0) {
			this.effect = group.hotEffect;
			this.oppositeEffect = group.coldEffect;
		} else if (temperatureLevel < 0) {
			this.effect = group.coldEffect;
			this.oppositeEffect = group.hotEffect;
		}
	}

	public Effect getEffect() {
		return this.effect.get();
	}

	public Effect getOppositeEffect() {
		return this.oppositeEffect.get();
	}
}
