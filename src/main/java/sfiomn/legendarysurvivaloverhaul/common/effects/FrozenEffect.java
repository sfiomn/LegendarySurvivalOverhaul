package sfiomn.legendarysurvivaloverhaul.common.effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public class FrozenEffect extends GenericEffect
{
	public FrozenEffect()
	{
		super(15725055, EffectType.HARMFUL);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
	
	public boolean shouldRender(EffectInstance effect) { return false; }
	
	public boolean shouldRenderInvText(EffectInstance effect) { return false; }
	
	public boolean shouldRenderHUD(EffectInstance effect) { return false; }
}
