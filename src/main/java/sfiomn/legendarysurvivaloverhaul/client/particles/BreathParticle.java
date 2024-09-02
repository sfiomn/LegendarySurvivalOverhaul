package sfiomn.legendarysurvivaloverhaul.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import javax.annotation.Nullable;

public class BreathParticle extends SpriteTexturedParticle {

    protected final IAnimatedSprite animatedSprite;

    public BreathParticle(IAnimatedSprite animatedSprite, ClientWorld level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);

        this.setSpriteFromAge(animatedSprite);
        this.animatedSprite = animatedSprite;

        this.lifetime = (int) (10.0D / (random.nextDouble() + 0.3D));

        this.alpha = 0.4f;
        this.gravity = 0.2f;
        this.scale(0.25f);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed)
            this.setSpriteFromAge(this.animatedSprite);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {

        private final IAnimatedSprite animatedSprite;

        public Factory(IAnimatedSprite animatedSprite) {
            this.animatedSprite = animatedSprite;
        }

        @Nullable
        @Override
        public Particle createParticle(BasicParticleType type, ClientWorld level, double x, double y, double z, double xd, double yd, double zd) {
            return new BreathParticle(this.animatedSprite, level, x, y, z, xd, yd, zd);
        }
    }
}