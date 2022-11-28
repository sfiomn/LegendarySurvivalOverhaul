package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

import java.util.UUID;

public class ExhaustionEffect extends GenericEffect
{
	public static final UUID SLOWNESS_ATTRIBUTE_UUID = UUID.fromString("f10d19a9-56db-45e6-9d43-ee250a06d7fd");
	
	public ExhaustionEffect()
	{
		super(5926017, EffectType.HARMFUL);
		this
			.addAttributeModifier(Attributes.MOVEMENT_SPEED, SLOWNESS_ATTRIBUTE_UUID.toString(), -0.30, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
	
	public boolean shouldRender(EffectInstance effect) { return false; }
	
	public boolean shouldRenderInvText(EffectInstance effect) { return false; }
	
	public boolean shouldRenderHUD(EffectInstance effect) { return false; }
	
}
