package me.desht.modularrouters.util;

import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CodecUtil {
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackHandler> ITEM_HANDLER_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ItemStackHandler decode(RegistryFriendlyByteBuf buf) {
            return createHandler(buf.registryAccess(), buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ItemStackHandler handler) {
            buf.writeNbt(handler.serializeNBT(buf.registryAccess()));
        }
    };

    private static ItemStackHandler createHandler(RegistryAccess registryAccess, CompoundTag tag) {
        return Util.make(new ItemStackHandler(), h -> h.deserializeNBT(registryAccess, tag));
    }
}
