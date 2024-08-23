package sfiomn.legendarysurvivaloverhaul.common.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

//  Deeply inspired by the tall AloeVera from MinecraftAbnormals
//  https://github.com/team-abnormals/atmospheric/blob/1.16.x/src/main/java/com/minecraftabnormals/atmospheric/client/particle/AloeBlossomParticle.java
public class FernBlossomParticle extends SpriteTexturedParticle {
    protected final IAnimatedSprite animatedSprite;
    private float angle;

    protected FernBlossomParticle(IAnimatedSprite animatedSprite, ClientWorld level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);

        this.setSpriteFromAge(animatedSprite);
        this.animatedSprite = animatedSprite;

        this.angle = this.random.nextFloat() * ((float) Math.PI * 2F);

        //  Duration of particle in ticks
        this.lifetime = this.random.nextInt(75) + 75;

        //  Scaling
        this.quadSize *= (2.0F + this.random.nextFloat() * 0.5);

        //  Motion of particles
        this.xd = xd;
        this.yd = yd + (this.random.nextDouble() * 0.05D);
        this.zd = zd;

        //  Color
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age % 5 == 0) {
            this.angle = (float) Math.random() * ((float) Math.PI * 2F);
        }
        this.xd += Math.cos(this.angle) * 0.0005;
        this.zd += Math.sin(this.angle) * 0.0005;
        if (!this.removed)
            this.setSpriteFromAge(this.animatedSprite);
        fadeOut();
    }

    private void fadeOut() {
        if (((float) age / lifetime) < 0.5f) {
            this.alpha = 1;
        } else {
            this.alpha = 1 - (((float) age / lifetime - 0.5f) * 2.0f);
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        float f = this.lifetime / (((this.age + (this.lifetime * 0.5F)) + partialTick));
        f = MathHelper.clamp(f, 0F, 0.5F);
        int i = super.getLightColor(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int) (f * 15f * 16f);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
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
            return new FernBlossomParticle(this.animatedSprite, level, x, y, z, xd, yd, zd);
        }
    }
}
