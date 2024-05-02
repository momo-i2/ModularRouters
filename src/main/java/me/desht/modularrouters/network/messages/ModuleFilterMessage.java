package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.container.BulkItemFilterMenu;
import me.desht.modularrouters.container.FilterSlot;
import me.desht.modularrouters.container.ModuleMenu;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Received on: SERVER
 * <p>
 * Sent by client when a filter slot is updated via the JEI ghost handler
 */
public record ModuleFilterMessage(int slot, ItemStack stack) implements CustomPacketPayload {
    public static final Type<ModuleFilterMessage> TYPE = new Type<>(MiscUtil.RL("module_filter"));

    public static final StreamCodec<RegistryFriendlyByteBuf,ModuleFilterMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ModuleFilterMessage::slot,
            ItemStack.STREAM_CODEC, ModuleFilterMessage::stack,
            ModuleFilterMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(ModuleFilterMessage message, IPayloadContext context) {
        AbstractContainerMenu c = context.player().containerMenu;
        int slot = message.slot();
        if (isValidContainer(c) && slot >= 0 && slot < c.slots.size() && c.getSlot(slot) instanceof FilterSlot) {
            c.getSlot(slot).set(message.stack());
        }
    }

    private static boolean isValidContainer(AbstractContainerMenu c) {
        return c instanceof ModuleMenu || c instanceof BulkItemFilterMenu;
    }
}
