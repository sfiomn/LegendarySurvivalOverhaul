package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.fml.RegistryObject;

import java.util.UUID;

public class AttributeBuilder {

    protected final RegistryObject<Attribute> attribute;
    protected final String descriptionId;

    public AttributeBuilder(RegistryObject<Attribute> attribute, String descriptionId) {
        this.attribute = attribute;
        this.descriptionId = descriptionId;
    }

    public void addModifier(ItemAttributeModifierEvent event, UUID uuid, double value) {
        event.addModifier(attribute.get(), new AttributeModifier(uuid, descriptionId, value, AttributeModifier.Operation.ADDITION));
    }

    public void addModifier(PlayerEntity player, UUID uuid, double value) {
        ModifiableAttributeInstance instance = player.getAttribute(attribute.get());
        if (instance != null) {
            instance.removeModifier(uuid);
            instance.addPermanentModifier(new AttributeModifier(uuid, descriptionId, value, AttributeModifier.Operation.ADDITION));
        }
    }

    public void removeModifier(PlayerEntity player, UUID uuid) {
        ModifiableAttributeInstance instance = player.getAttribute(attribute.get());
        if (instance != null) {
            instance.removeModifier(uuid);
        }
    }
}
