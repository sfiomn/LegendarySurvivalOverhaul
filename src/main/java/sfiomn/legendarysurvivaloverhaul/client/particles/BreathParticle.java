package sfiomn.legendarysurvivaloverhaul.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BreathParticle extends TextureSheetParticle {

    protected final SpriteSet animatedSprite;

    public BreathParticle(SpriteSet animatedSprite, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);
        this.xd *= 0.1f;
        this.yd *= 0.1f;
        this.zd *= 0.1f;

        this.setSpriteFromAge(animatedSprite);
        this.animatedSprite = animatedSprite;

        this.lifetime = (int) (10.0D / (random.nextDouble() + 0.3D));

        this.alpha = 0.25f;
        this.friction = 0.8F;
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.animatedSprite);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet animatedSprite;

        public Factory(SpriteSet animatedSprite) {
            this.animatedSprite = animatedSprite;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new BreathParticle(this.animatedSprite, level, x, y, z, xd, yd, zd);
        }
    }
}