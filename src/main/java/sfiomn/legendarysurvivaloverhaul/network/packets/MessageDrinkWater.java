package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;

import java.util.function.Supplier;

public class MessageDrinkWater
{
    // SERVER side message

    public MessageDrinkWater()
    {
    }

    public static MessageDrinkWater decode(FriendlyByteBuf buffer)
    {
        return new MessageDrinkWater();
    }

    public static void encode(MessageDrinkWater message, FriendlyByteBuf buffer)
    {
    }

    public static void handle(MessageDrinkWater message, Supplier<NetworkEvent.Context> supplier)
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
        HydrationEnum traceWater = ThirstUtil.traceWater(player);

        if (traceWater == null)
            return;

        ThirstUtil.takeDrink(player, traceWater);
    }
}
