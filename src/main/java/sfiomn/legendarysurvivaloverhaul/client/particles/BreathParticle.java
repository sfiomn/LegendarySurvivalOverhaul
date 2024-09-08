package sfiomn.legendarysurvivaloverhaul.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import javax.annotation.Nullable;

public class BreathParticle extends SpriteTexturedParticle {

    protected final IAnimatedSprite animatedSprite;

    public BreathParticle(IAnimatedSprite animatedSprite, ClientWorld level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);
        this.xd *= 0.1f;
        this.yd *= 0.1f;
        this.zd *= 0.1f;

        this.setSpriteFromAge(animatedSprite);
        this.animatedSprite = animatedSprite;

        this.lifetime = (int) (10.0D / (random.nextDouble() + 0.3D));

        this.alpha = 0.25f;
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.04 * (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.8;
            this.yd *= 0.8;
            this.zd *= 0.8;
            if (this.onGround) {
                this.xd *= 0.699999988079071;
                this.zd *= 0.699999988079071;
            }
        }
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