package sfiomn.legendarysurvivaloverhaul.common.items;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ThermometerItem extends Item {
    public ThermometerItem(Item.Properties properties){
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            TemperatureItemCapability tempCap = CapabilityUtil.getTempItemCapability(player.getMainHandItem());

            float temperature = tempCap.getWorldTemperatureLevel();
            Component temperatureComponent;
            if (Config.Baked.renderTemperatureInFahrenheit) {
                temperatureComponent = Component.literal(WorldUtil.toFahrenheit(temperature) + "\u00B0F");
            } else {
                temperatureComponent = Component.literal(temperature + "\u00B0C");
            }
            player.displayClientMessage(temperatureComponent, (true));
        }
        return super.use(level, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new TemperatureItemCapability.TemperatureItemProvider();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        List<MutableComponent> text = new ArrayList<>();

        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeyMappingRegistry.showAddedDesc.getKey().getValue())) {
            text.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".thermometer.description"));
            if (LegendarySurvivalOverhaul.curiosLoaded)
                text.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".thermometer.bauble_description"));

        } else {
            text.add(Component.literal(ChatFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", ChatFormatting.LIGHT_PURPLE, I18n.get(KeyMappingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), ChatFormatting.GRAY)));
        }

        tooltipComponents.addAll(text);
    }
}
