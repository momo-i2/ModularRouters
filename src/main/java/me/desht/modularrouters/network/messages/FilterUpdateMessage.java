package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.item.smartfilter.SmartFilterItem;
import me.desht.modularrouters.util.MFLocator;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Received on: SERVER
 * <p>
 * Sent by client when most filters (not the bulk filter) are updated client-side
 * The filter could be in a player's hand, or in a module (which may or may not be in a router...)
 */
public record FilterUpdateMessage(MFLocator locator, ItemStack newFilterStack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FilterUpdateMessage> TYPE = new CustomPacketPayload.Type<>(MiscUtil.RL("filter_update"));

    public static final StreamCodec<RegistryFriendlyByteBuf,FilterUpdateMessage> STREAM_CODEC = StreamCodec.composite(
            MFLocator.STREAM_CODEC, FilterUpdateMessage::locator,
            ItemStack.STREAM_CODEC, FilterUpdateMessage::newFilterStack,
            FilterUpdateMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(FilterUpdateMessage message, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        MFLocator locator = message.locator();
        ItemStack filterStack = locator.getTargetItem(player);

        if (filterStack.getItem() instanceof SmartFilterItem && filterStack.getItem() == message.newFilterStack.getItem()) {
            locator.setFilterStack(player, message.newFilterStack);
            PacketDistributor.sendToPlayer(player, new GuiSyncMessage(message.newFilterStack));
        }
    }
}
