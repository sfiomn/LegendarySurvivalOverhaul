package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

import java.util.function.Supplier;

public class MessageBodyPartHealingItem
{
    private CompoundNBT compound;
    // SERVER side message

    public MessageBodyPartHealingItem(BodyPartEnum bodyPart, Hand hand, boolean consumeItem)
    {
        CompoundNBT bodyPartHealNbt = new CompoundNBT();
        bodyPartHealNbt.putString("bodyPartEnum", bodyPart.name());
        bodyPartHealNbt.putBoolean("mainHand", hand == Hand.MAIN_HAND);
        bodyPartHealNbt.putBoolean("consumeItem", consumeItem);
        this.compound = bodyPartHealNbt;
    }

    public MessageBodyPartHealingItem(CompoundNBT nbt) {
        this.compound = nbt;
    }

    public MessageBodyPartHealingItem() {}

    public static MessageBodyPartHealingItem decode(PacketBuffer buffer)
    {
        return new MessageBodyPartHealingItem(buffer.readNbt());
    }

    public static void encode(MessageBodyPartHealingItem message, PacketBuffer buffer) {
        buffer.writeNbt(message.compound);
    }

    public static void handle(MessageBodyPartHealingItem message, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                context.enqueueWork(() -> applyHealingItemOnServer(player, message.compound));
            }
        }
        supplier.get().setPacketHandled(true);
    }

    public static void applyHealingItemOnServer(ServerPlayerEntity player, CompoundNBT nbt) {
        BodyPartEnum bodyPartEnum = BodyPartEnum.valueOf(nbt.getString("bodyPartEnum"));
        Hand hand = nbt.getBoolean("mainHand") ? Hand.MAIN_HAND: Hand.OFF_HAND;

        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.getItem() instanceof BodyHealingItem) {
            player.level.playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            BodyDamageUtil.applyHealingItem(player, bodyPartEnum, (BodyHealingItem) itemStack.getItem());

            if (nbt.getBoolean("consumeItem")) {
                ((BodyHealingItem) itemStack.getItem()).runSecondaryEffect(player, itemStack);
                if (!player.isCreative())
                    itemStack.shrink(1);
            }
        }
    }
}
