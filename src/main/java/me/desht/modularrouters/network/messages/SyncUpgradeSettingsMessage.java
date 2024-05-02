package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.item.upgrade.SyncUpgrade;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Received on: SERVER
 * <p>
 * Sent by client when a new tuning value is entered via Sync Upgrade GUI
 */
public record SyncUpgradeSettingsMessage(int tunedValue, InteractionHand hand) implements CustomPacketPayload {
    public static final Type<SyncUpgradeSettingsMessage> TYPE = new Type<>(MiscUtil.RL("sync_upgrade_settings"));

    public static final StreamCodec<FriendlyByteBuf,SyncUpgradeSettingsMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncUpgradeSettingsMessage::tunedValue,
            NeoForgeStreamCodecs.enumCodec(InteractionHand.class), SyncUpgradeSettingsMessage::hand,
            SyncUpgradeSettingsMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(SyncUpgradeSettingsMessage message, IPayloadContext context) {
        ItemStack held = context.player().getItemInHand(message.hand());
        if (held.getItem() instanceof SyncUpgrade) {
            SyncUpgrade.setTunedValue(held, message.tunedValue());
        }
    }
}
