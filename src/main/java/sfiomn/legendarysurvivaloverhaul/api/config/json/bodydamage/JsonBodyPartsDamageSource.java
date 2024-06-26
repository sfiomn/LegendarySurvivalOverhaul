package sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
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

    public List<BodyPartEnum> getBodyParts(Player player) {
        return this.damageDistribution.getBodyParts(player, this.bodyParts);
    }
}
