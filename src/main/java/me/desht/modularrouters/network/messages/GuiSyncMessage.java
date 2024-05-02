package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.client.util.ClientUtil;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Received on: CLIENT
 * <p>
 * Sent when a filter is changed server-side which requires an open GUI to re-read its settings.
 */
public record GuiSyncMessage(ItemStack newStack) implements CustomPacketPayload {
    public static final Type<GuiSyncMessage> TYPE = new Type<>(MiscUtil.RL("gui_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf,GuiSyncMessage> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, GuiSyncMessage::newStack,
            GuiSyncMessage::new
    );

    public static void handleData(GuiSyncMessage message, IPayloadContext context) {
        ClientUtil.resyncGui(message.newStack());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
