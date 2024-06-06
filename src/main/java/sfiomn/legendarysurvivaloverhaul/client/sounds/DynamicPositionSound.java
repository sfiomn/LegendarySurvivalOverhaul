package sfiomn.legendarysurvivaloverhaul.client.sounds;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class DynamicPositionSound extends TickableSound {
    private final PlayerEntity player;
    private int time;

    protected DynamicPositionSound(SoundEvent soundEvent, PlayerEntity player) {
        super(soundEvent, SoundCategory.PLAYERS);
        this.player = player;
        this.looping = false;
        this.volume = 1.0F;
        this.time = 60;
    }

    @Override
    public void tick() {
        if (this.player.isAlive() && (this.time-- >= 0)) {
            this.x = this.player.getX();
            this.y = this.player.getY();
            this.z = this.player.getZ();

        } else {
            this.stop();
        }
    }
}