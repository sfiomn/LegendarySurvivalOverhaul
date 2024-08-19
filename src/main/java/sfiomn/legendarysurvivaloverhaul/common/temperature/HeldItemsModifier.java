package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
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
    public float getPlayerInfluence(Player player)
    {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        float sum = 0.0f;

        if(!mainHand.isEmpty())
        {
            sum += processStackJson(mainHand);
        }

        if(!offHand.isEmpty())
        {
            sum += processStackJson(offHand);
        }

        return sum;
    }

    private float processStackJson(ItemStack stack)
    {
        // If held item is armor, ignore it
        if (stack.getItem() instanceof ArmorItem || stack.getItem() instanceof ICurioItem)
            return 0.0f;

        ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());

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
