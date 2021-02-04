package icey.survivaloverhaul.client.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlayerDripParticle extends SpriteTexturedParticle
{

	protected PlayerDripParticle(ClientWorld world, double posX, double posY, double posZ)
	{
		super(world, posX, posY, posZ, 0.0d, 0.0d, 0.0d);
		this.motionX *= 0.01d;
		this.motionY *= 0.01d;
		this.motionZ *= 0.01d;
		this.motionY += 0.1d;
		this.particleScale = 0.5f;
		this.maxAge = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
		this.canCollide = true;
	}

	@Override
	public IParticleRenderType getRenderType()
	{
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
	
	public void tick()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.ageParticle();
		if (!this.isExpired)
		{
			
		}
	}
	
	private void ageParticle()
	{
		if (this.maxAge-- <= 0)
			this.setExpired();
	}
}
