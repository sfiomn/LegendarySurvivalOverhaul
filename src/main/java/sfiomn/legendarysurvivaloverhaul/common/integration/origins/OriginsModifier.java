package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;


public class OriginsModifier extends ModifierBase {
    public OriginsModifier() {}

    @Override
    public float getPlayerInfluence(Player player) {

        if (!LegendarySurvivalOverhaul.originsLoaded)
            return 0.0f;

        OriginComponent component = ModComponents.ORIGIN.get(player);

        float temp = 0.0f;

        for (Origin origin : component.getOrigins().values()) {
            JsonTemperatureResistance config = TemperatureDataManager.getOrigin(origin.getIdentifier());
            temp += config != null ? config.temperature : 0;
        }

        return temp;
    }
}
