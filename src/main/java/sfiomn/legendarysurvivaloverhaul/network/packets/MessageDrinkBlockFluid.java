package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
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

    public static MessageDrinkBlockFluid decode(PacketBuffer buffer)
    {
        return new MessageDrinkBlockFluid();
    }

    public static void encode(MessageDrinkBlockFluid message, PacketBuffer buffer)
    {
    }

    public static void handle(MessageDrinkBlockFluid message, Supplier<NetworkEvent.Context> supplier)
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
        JsonBlockFluidThirst traceWater = ThirstUtil.getJsonBlockFluidThirstLookedAt(player, player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()) / 2);

        if (traceWater == null)
            return;

        ThirstUtil.takeDrink(player, traceWater.hydration, traceWater.saturation, traceWater.effects);
    }
}
