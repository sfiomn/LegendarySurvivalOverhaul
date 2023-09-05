package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class ColdHungerEffect extends GenericEffect {

    public ColdHungerEffect()
    {
        super(10870382, EffectType.HARMFUL);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier)
    {
        if(entity instanceof PlayerEntity)
        {
            ((PlayerEntity)entity).causeFoodExhaustion(Config.Baked.coldHungerEffectModifier * (float)(amplifier + 1));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // Apply thirsty effect every 50 ticks for amplifier 0
        int time = 50 >> amplifier;
        return time == 0 || duration % time == 0;
    }
}
