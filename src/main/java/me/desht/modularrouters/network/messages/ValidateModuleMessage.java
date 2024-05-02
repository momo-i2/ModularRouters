package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.item.module.ModuleItem;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Received on: SERVER
 * <p>
 * Sent by client when a module is left-clicked; ask the server to validate the module and
 * send the player a message.
 */
public record ValidateModuleMessage(InteractionHand hand) implements CustomPacketPayload {
    public static final Type<ValidateModuleMessage> TYPE = new Type<>(MiscUtil.RL("validate_module"));
    public static final StreamCodec<FriendlyByteBuf,ValidateModuleMessage> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(InteractionHand.class), ValidateModuleMessage::hand,
            ValidateModuleMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(ValidateModuleMessage message, IPayloadContext context) {
        ItemStack stack = context.player().getItemInHand(message.hand());
        if (stack.getItem() instanceof ModuleItem moduleItem && context.player() instanceof ServerPlayer sp) {
            moduleItem.doModuleValidation(stack, sp);
        }
    }
}
