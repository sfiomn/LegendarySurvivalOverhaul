package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

public class AttributeBuilder {

    protected final Attribute attribute;
    protected final String descriptionId;

    public AttributeBuilder(Attribute attribute, String descriptionId) {
        this.attribute = attribute;
        this.descriptionId = descriptionId;
    }

    public void addModifier(ItemAttributeModifierEvent event, UUID uuid, double value) {
        event.addModifier(attribute, new AttributeModifier(uuid, descriptionId, value, AttributeModifier.Operation.ADDITION));
    }

    public void addModifier(Player player, UUID uuid, double value) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removePermanentModifier(uuid);
            instance.addPermanentModifier(new AttributeModifier(uuid, descriptionId, value, AttributeModifier.Operation.ADDITION));
        }
    }
}
