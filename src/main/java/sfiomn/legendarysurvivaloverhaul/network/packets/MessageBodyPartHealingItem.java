package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

import java.util.function.Supplier;

public class MessageBodyPartHealingItem
{
    private CompoundTag compound;
    // SERVER side message

    public MessageBodyPartHealingItem(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem)
    {
        CompoundTag bodyPartHealNbt = new CompoundTag();
        bodyPartHealNbt.putString("bodyPartEnum", bodyPart.name());
        bodyPartHealNbt.putBoolean("mainHand", hand == InteractionHand.MAIN_HAND);
        bodyPartHealNbt.putBoolean("consumeItem", consumeItem);
        this.compound = bodyPartHealNbt;
    }

    public MessageBodyPartHealingItem(Tag nbt) {
        this.compound = (CompoundTag) nbt;
    }

    public MessageBodyPartHealingItem() {}

    public static MessageBodyPartHealingItem decode(FriendlyByteBuf buffer)
    {
        return new MessageBodyPartHealingItem(buffer.readNbt());
    }

    public static void encode(MessageBodyPartHealingItem message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.compound);
    }

    public static void handle(MessageBodyPartHealingItem message, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayer player = context.getSender();
            if (player != null) {
                context.enqueueWork(() -> applyHealingItemOnServer(player, message.compound));
            }
        }
        supplier.get().setPacketHandled(true);
    }

    public static void applyHealingItemOnServer(ServerPlayer player, CompoundTag nbt) {
        BodyPartEnum bodyPartEnum = BodyPartEnum.valueOf(nbt.getString("bodyPartEnum"));
        InteractionHand hand = nbt.getBoolean("mainHand") ? InteractionHand.MAIN_HAND: InteractionHand.OFF_HAND;

        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.getItem() instanceof BodyHealingItem) {
            player.serverLevel().playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            BodyDamageUtil.applyHealingItem(player, bodyPartEnum, (BodyHealingItem) itemStack.getItem());

            if (nbt.getBoolean("consumeItem")) {
                ((BodyHealingItem) itemStack.getItem()).runSecondaryEffect(player, itemStack);
                if (!player.isCreative())
                    itemStack.shrink(1);
            }
        }
    }
}
