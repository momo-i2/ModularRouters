package me.desht.modularrouters.logic.settings;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface SyncableSettings<T> {
    StreamCodec<FriendlyByteBuf,T> streamCodec();
}
