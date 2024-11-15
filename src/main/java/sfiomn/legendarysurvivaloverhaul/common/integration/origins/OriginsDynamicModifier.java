package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.forge.ModComponentsArchitecturyImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.integration.json.JsonIntegrationConfig;

public class OriginsDynamicModifier extends DynamicModifierBase {
    public OriginsDynamicModifier() {}

    @Override
    public float applyDynamicPlayerInfluence(PlayerEntity player, float currentTemperature, float currentResistance) {

        if (!LegendarySurvivalOverhaul.originsLoaded)
            return 0.0f;

        float effectiveResistance = 0.0f;
        float diffToAverage = currentTemperature - TemperatureEnum.NORMAL.getMiddle();

        LazyOptional<OriginComponent> optionalOrigin = player.getCapability(ModComponentsArchitecturyImpl.ORIGIN_COMPONENT_CAPABILITY);
        if (optionalOrigin.isPresent() && optionalOrigin.resolve().isPresent()) {
            OriginComponent origins = optionalOrigin.resolve().get();
            for (Origin origin : origins.getOrigins().values()) {
                if (JsonIntegrationConfig.originsTemperatures.containsKey(origin.getIdentifier().toString())) {
                    JsonTemperatureResistance config = JsonIntegrationConfig.originsTemperatures.get(origin.getIdentifier().toString());

                    double maxResistance = config.thermalResistance;

                    if (diffToAverage > 0) {
                        maxResistance += config.heatResistance;
                        effectiveResistance = (float) MathHelper.clamp(maxResistance, currentResistance, diffToAverage + currentResistance);
                        effectiveResistance = -effectiveResistance;
                    } else if (diffToAverage < 0) {
                        maxResistance += config.coldResistance;
                        diffToAverage = -diffToAverage;
                        currentResistance = -currentResistance;
                        effectiveResistance = (float) MathHelper.clamp(maxResistance, currentResistance, diffToAverage + currentResistance);
                    }

                }
            }
        }

        return effectiveResistance;
    }
}
