package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.integration.json.JsonIntegrationConfig;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

public class OriginsDynamicModifier extends DynamicModifierBase {
    public OriginsDynamicModifier() {}

    @Override
    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance) {

        if (!LegendarySurvivalOverhaul.originsLoaded)
            return 0.0f;

        float effectiveResistance = 0.0f;
        float diffToAverage = currentTemperature - TemperatureEnum.NORMAL.getMiddle();

        LazyOptional<IOriginContainer> optionalOrigin = player.getCapability(OriginsAPI.ORIGIN_CONTAINER);
        if (optionalOrigin.isPresent() && optionalOrigin.resolve().isPresent()) {
            IOriginContainer origins = optionalOrigin.resolve().get();
            for (ResourceKey<Origin> origin : origins.getOrigins().values()) {
                if (JsonIntegrationConfig.originsTemperatures.containsKey(origin.location().toString())) {
                    JsonTemperatureResistance config = JsonIntegrationConfig.originsTemperatures.get(origin.location().toString());

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
        }

        return effectiveResistance;
    }
}
