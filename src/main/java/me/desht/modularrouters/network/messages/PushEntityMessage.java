package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.client.util.ClientUtil;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Received on: CLIENT
 * <p>
 * Sent by server so clients promptly update an entity's velocity when it gets shoved by an extruded block.
 */
public record PushEntityMessage(int entityId, Vec3 vec) implements CustomPacketPayload {
    public static final Type<PushEntityMessage> TYPE = new Type<>(MiscUtil.RL("push_entity"));

    public static final StreamCodec<FriendlyByteBuf,PushEntityMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PushEntityMessage::entityId,
            ByteBufCodecs.fromCodec(Vec3.CODEC), PushEntityMessage::vec,
            PushEntityMessage::new
    );

    public static PushEntityMessage forEntity(Entity entity, Vec3 vec) {
        return new PushEntityMessage(entity.getId(), vec);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(PushEntityMessage message, IPayloadContext context) {
        Entity entity = ClientUtil.theClientLevel().getEntity(message.entityId());
        if (entity != null) {
            Vec3 vec = message.vec();
            entity.setDeltaMovement(vec.x, vec.y, vec.z);
            entity.horizontalCollision = false;
            entity.verticalCollision = false;
            if (entity instanceof LivingEntity l) l.setJumping(true);
        }
    }
}
