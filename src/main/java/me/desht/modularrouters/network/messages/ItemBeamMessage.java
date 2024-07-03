package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.client.util.ClientUtil;
import me.desht.modularrouters.core.ModBlockEntities;
import me.desht.modularrouters.util.BeamData;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

/**
 * Received on: CLIENT
 * <p>
 * Sent by server to play an item beam between a router and another inventory
 */
public record ItemBeamMessage(BlockPos pos, List<BeamData> beams) implements CustomPacketPayload {
    public static final Type<ItemBeamMessage> TYPE = new Type<>(MiscUtil.RL("item_beam"));

    public static final StreamCodec<RegistryFriendlyByteBuf,ItemBeamMessage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ItemBeamMessage::pos,
            BeamData.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemBeamMessage::beams,
            ItemBeamMessage::new
    );

    public static ItemBeamMessage create(BlockPos pos, List<BeamData> beams) {
        return new ItemBeamMessage(pos, List.copyOf(beams));
    }

    public static void handleData(ItemBeamMessage message, IPayloadContext context) {
        context.player().level().getBlockEntity(message.pos(), ModBlockEntities.MODULAR_ROUTER.get())
                .ifPresent(te -> message.beams().forEach(te::addItemBeam));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
