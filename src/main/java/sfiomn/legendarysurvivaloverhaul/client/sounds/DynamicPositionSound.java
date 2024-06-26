package sfiomn.legendarysurvivaloverhaul.client.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class DynamicPositionSound extends AbstractTickableSoundInstance {
    private final Player player;
    private int time;

    protected DynamicPositionSound(SoundEvent soundEvent, Player player) {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
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