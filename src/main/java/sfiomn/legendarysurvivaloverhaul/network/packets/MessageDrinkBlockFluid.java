package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;

import java.util.function.Supplier;

public class MessageDrinkBlockFluid
{
    // SERVER side message

    public MessageDrinkBlockFluid()
    {
    }

    public static MessageDrinkBlockFluid decode(FriendlyByteBuf buffer)
    {
        return new MessageDrinkBlockFluid();
    }

    public static void encode(MessageDrinkBlockFluid message, FriendlyByteBuf buffer)
    {
    }

    public static void handle(MessageDrinkBlockFluid message, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayer player = context.getSender();
            if (player != null) {
                context.enqueueWork(() -> DrinkWaterOnServer(player));
            }
        }
        supplier.get().setPacketHandled(true);
    }

    public static void DrinkWaterOnServer(ServerPlayer player) {
        JsonBlockFluidThirst jsonBlockFluidThirst = ThirstUtil.getJsonBlockFluidThirstLookedAt(player, player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2);

        if (jsonBlockFluidThirst == null)
            return;

        ThirstUtil.takeDrink(player, jsonBlockFluidThirst.hydration, jsonBlockFluidThirst.saturation, jsonBlockFluidThirst.effects);
    }
}
