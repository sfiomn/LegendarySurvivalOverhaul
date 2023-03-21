package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

public class ThermometerItem extends Item {
    private long updateTime;
    private float worldTemp = 0.0f;

    public ThermometerItem(Properties properties){
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        long currTime = System.currentTimeMillis();

        if ((currTime - updateTime) > 500) {
            updateTime = currTime;
            worldTemp = TemperatureUtil.getWorldTemperature(world, player.blockPosition());
        }
        player.displayClientMessage(new StringTextComponent(worldTemp + "\u00B0C"), (true));
        return super.use(world, player, hand);
    }
}
