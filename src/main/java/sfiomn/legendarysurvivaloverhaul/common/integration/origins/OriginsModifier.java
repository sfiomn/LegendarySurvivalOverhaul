package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.forge.ModComponentsArchitecturyImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.json.JsonIntegrationConfig;


public class OriginsModifier extends ModifierBase {
    public OriginsModifier() {}

    @Override
    public float getPlayerInfluence(PlayerEntity player) {

        if (!LegendarySurvivalOverhaul.originsLoaded)
            return 0.0f;

        LazyOptional<OriginComponent> optionalOrigin = player.getCapability(ModComponentsArchitecturyImpl.ORIGIN_COMPONENT_CAPABILITY);

        float temp = 0.0f;

        if (optionalOrigin.isPresent() && optionalOrigin.resolve().isPresent()) {
            OriginComponent origins = optionalOrigin.resolve().get();
            for (Origin origin : origins.getOrigins().values()) {
                if (JsonIntegrationConfig.originsTemperatures.containsKey(origin.getIdentifier().toString())) {
                    temp += JsonIntegrationConfig.originsTemperatures.get(origin.getIdentifier().toString()).temperature;
                }
            }
        }

        return temp;
    }
}
