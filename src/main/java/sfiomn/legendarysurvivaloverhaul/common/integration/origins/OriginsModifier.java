package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.json.JsonIntegrationConfig;


public class OriginsModifier extends ModifierBase {
    public OriginsModifier() {}

    @Override
    public float getPlayerInfluence(Player player) {

        LazyOptional<IOriginContainer> optionalOrigin = player.getCapability(OriginsAPI.ORIGIN_CONTAINER);

        float temp = 0.0f;

        if (optionalOrigin.isPresent() && optionalOrigin.resolve().isPresent()) {
            IOriginContainer origins = optionalOrigin.resolve().get();
            for (ResourceKey<Origin> origin : origins.getOrigins().values()) {
                if (JsonIntegrationConfig.originsTemperatures.containsKey(origin.location().toString())) {
                    temp += JsonIntegrationConfig.originsTemperatures.get(origin.location().toString()).temperature;
                }
            }
        }

        return temp;
    }
}
