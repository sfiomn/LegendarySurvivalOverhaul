package sfiomn.legendarysurvivaloverhaul.client.sounds;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class TemperatureEffectSound extends TickableSound {
    private final PlayerEntity player;
    private int time;

    public TemperatureEffectSound(PlayerEntity player, SoundEvent soundEvent) {
        super(soundEvent, SoundCategory.PLAYERS);
        this.player = player;
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
    }

    public void tick() {
        ++this.time;
        if (this.player.isAlive() && (this.time <= 20 || shouldPlaySound())) {
            this.x = this.player.getX();
            this.y = this.player.getY();
            this.z = this.player.getZ();

        } else {
            this.stop();
        }
    }

    protected abstract boolean shouldPlaySound();
}
