package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class HeldItemsModifier extends ModifierBase {
    public HeldItemsModifier()
    {
        super();
    }

    @Override
    public float getPlayerInfluence(PlayerEntity player)
    {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        float sum = 0.0f;

        if(!mainHand.isEmpty())
        {
            sum += processStack(mainHand);
        }

        if(!offHand.isEmpty())
        {
            sum += processStack(offHand);
        }

        return sum;
    }

    private float processStack(ItemStack stack)
    {
        // If held item is armor, ignore it
        if (stack.getItem() instanceof ArmorItem || stack.getItem() instanceof ICurioItem)
            return 0.0f;

        ResourceLocation itemRegistryName = stack.getItem().getRegistryName();

        JsonTemperature jsonTemperature = null;
        if (itemRegistryName != null)
            jsonTemperature = JsonConfig.itemTemperatures.get(itemRegistryName.toString());

        if (jsonTemperature != null)
        {
            return jsonTemperature.temperature;
        }

        return 0.0f;
    }
}
