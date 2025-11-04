package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;

public class OriginsDynamicModifier extends DynamicModifierBase {
    public OriginsDynamicModifier() {
    }

    @Override
    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance) {

        if (!LegendarySurvivalOverhaul.originsLoaded)
            return 0.0f;

        float effectiveResistance = 0.0f;
        float diffToAverage = currentTemperature - TemperatureEnum.NORMAL.getMiddle();

        OriginComponent component = ModComponents.ORIGIN.get(player);
        for (Origin origin : component.getOrigins().values()) {
            JsonTemperatureResistance config = TemperatureDataManager.getOrigin(origin.getIdentifier());
            if (config != null) {
                double maxResistance = config.thermalResistance;
                if (diffToAverage > 0) {
                    maxResistance += config.heatResistance;
                    effectiveResistance = (float) Mth.clamp(maxResistance, currentResistance, diffToAverage + currentResistance);
                    effectiveResistance = -effectiveResistance;
                } else if (diffToAverage < 0) {
                    maxResistance += config.coldResistance;
                    diffToAverage = -diffToAverage;
                    currentResistance = -currentResistance;
                    effectiveResistance = (float) Mth.clamp(maxResistance, currentResistance, diffToAverage + currentResistance);
                }
            }
        }


        return effectiveResistance;
    }
}
