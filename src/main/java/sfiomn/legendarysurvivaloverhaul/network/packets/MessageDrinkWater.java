package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;

import java.util.function.Supplier;

public class MessageDrinkWater
{
    // SERVER side message

    public MessageDrinkWater()
    {
    }

    public static MessageDrinkWater decode(PacketBuffer buffer)
    {
        return new MessageDrinkWater();
    }

    public static void encode(MessageDrinkWater message, PacketBuffer buffer)
    {
    }

    public static void handle(MessageDrinkWater message, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                context.enqueueWork(() -> DrinkWaterOnServer(player));
            }
        }
        supplier.get().setPacketHandled(true);
    }

    public static void DrinkWaterOnServer(ServerPlayerEntity player) {
        HydrationEnum traceWater = ThirstUtil.traceWater(player);

        if (traceWater == null)
            return;

        ThirstUtil.takeDrink(player, traceWater);
    }
}
