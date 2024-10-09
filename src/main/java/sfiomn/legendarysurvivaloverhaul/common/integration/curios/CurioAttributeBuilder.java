package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;

import java.util.UUID;

public class CurioAttributeBuilder extends AttributeBuilder {

    public CurioAttributeBuilder(Attribute attribute, String descriptionId) {
        super(attribute, descriptionId);
    }

    public void addModifier(CurioAttributeModifierEvent event, UUID uuid, double value) {
        event.addModifier(this.attribute, new AttributeModifier(uuid, this.descriptionId, value, AttributeModifier.Operation.ADDITION));
    }
}
