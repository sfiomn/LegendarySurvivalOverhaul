package sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.player.PlayerEntity;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonBodyPartsDamageSource {

    @SerializedName("bodyParts")
    public List<BodyPartEnum> bodyParts;

    @SerializedName("damageDistribution")
    public DamageDistributionEnum damageDistribution;

    public JsonBodyPartsDamageSource(DamageDistributionEnum damageDistribution, List<BodyPartEnum> bodyParts) {
        this.damageDistribution = damageDistribution;
        this.bodyParts = bodyParts;
    }

    public List<BodyPartEnum> getBodyParts(PlayerEntity player) {
        return this.damageDistribution.getBodyParts(player, this.bodyParts);
    }
}
