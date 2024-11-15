package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.KeybindingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ThermometerItem extends Item {
    public ThermometerItem(Properties properties){
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClientSide()) {
            TemperatureItemCapability tempCap = CapabilityUtil.getTempItemCapability(player.getMainHandItem());

            float temperature = tempCap.getWorldTemperatureLevel();
            StringTextComponent temperatureComponent;
            if (Config.Baked.renderTemperatureInFahrenheit) {
                temperatureComponent = new StringTextComponent(WorldUtil.toFahrenheit(temperature) + "\u00B0F");
            } else {
                temperatureComponent = new StringTextComponent(temperature + "\u00B0C");
            }
            player.displayClientMessage(temperatureComponent, (true));
        }
        return super.use(world, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new TemperatureItemCapability.TemperatureItemProvider();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag isAdvanced) {
        super.appendHoverText(stack, world, tooltips, isAdvanced);

        if (world == null || !world.isClientSide)
            return;

        List<ITextComponent> text = new ArrayList<>();

        if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeybindingRegistry.showAddedDesc.getKey().getValue())) {
            text.add(new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".thermometer.description"));
            if (LegendarySurvivalOverhaul.curiosLoaded)
                text.add(new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".thermometer.bauble_description"));

        } else {
            text.add(new StringTextComponent(TextFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", TextFormatting.LIGHT_PURPLE, I18n.get(KeybindingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), TextFormatting.GRAY)));
        }

        tooltips.addAll(text);
    }
}
