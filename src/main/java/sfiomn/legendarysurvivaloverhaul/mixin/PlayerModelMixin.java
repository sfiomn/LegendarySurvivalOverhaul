package sfiomn.legendarysurvivaloverhaul.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin extends BipedModel<LivingEntity>
{
    public PlayerModelMixin(float modelSize)
    {
        super(modelSize);
    }

    public void setRotationAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYawn, float headPitch, CallbackInfo info)
    {

    }
}
