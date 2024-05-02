package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity.RecompileFlag;
import me.desht.modularrouters.container.handler.BaseModuleHandler;
import me.desht.modularrouters.item.smartfilter.BulkItemFilter;
import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.util.MFLocator;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

/**
 * Received on: SERVER
 * <p>
 * Sent when a bulk filter clear/load/merge button is pressed to modify its contents server-side
 * The filter could be in a player's hand, or in a module (which may or may not be in a router...)
 */
public record BulkFilterUpdateMessage(FilterOp op, MFLocator locator, Optional<ModuleTarget> targetInv) implements CustomPacketPayload {
    public static final Type<BulkFilterUpdateMessage> TYPE = new Type<>(MiscUtil.RL("filter_settings"));

    public static final StreamCodec<FriendlyByteBuf, BulkFilterUpdateMessage> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(FilterOp.class), BulkFilterUpdateMessage::op,
            MFLocator.STREAM_CODEC, BulkFilterUpdateMessage::locator,
            ModuleTarget.STREAM_CODEC.apply(ByteBufCodecs::optional), BulkFilterUpdateMessage::targetInv,
            BulkFilterUpdateMessage::new
    );

    public static BulkFilterUpdateMessage targeted(FilterOp op, MFLocator locator, ModuleTarget target) {
        return new BulkFilterUpdateMessage(op, locator, Optional.of(target));
    }

    public static BulkFilterUpdateMessage untargeted(FilterOp op, MFLocator locator) {
        return new BulkFilterUpdateMessage(op, locator, Optional.empty());
    }

    @Override
    public Type<BulkFilterUpdateMessage> type() {
        return TYPE;
    }

    public Optional<IItemHandler> getTargetInventory() {
        return targetInv.flatMap(ModuleTarget::getItemHandler);
    }

    public static void handleData(BulkFilterUpdateMessage message, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        MFLocator locator = message.locator();
        ItemStack moduleStack = locator.getModuleStack(player);
        ItemStack filterStack = locator.getTargetItem(player);
        if (filterStack.getItem() instanceof BulkItemFilter bulkFilter) {
            GuiSyncMessage response = bulkFilter.onReceiveSettingsMessage(player, message, moduleStack);
            if (!moduleStack.isEmpty()) {
                ModularRouterBlockEntity router = locator.getRouter(player.level()).orElse(null);
                BaseModuleHandler.ModuleFilterHandler filterHandler = new BaseModuleHandler.ModuleFilterHandler(moduleStack, router);
                filterHandler.setStackInSlot(locator.filterSlot(), filterStack);
                filterHandler.save();
                if (locator.hand() != null) {
                    player.setItemInHand(locator.hand(), filterHandler.getHolderStack());
                } else if (router != null) {
                    router.recompileNeeded(RecompileFlag.MODULES);
                }
            }
            if (response != null) {
                // send to any nearby players in case they also have the GUI open
                PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) player.level(), player.chunkPosition(), response);
            }
        }
    }

    public enum FilterOp {
        CLEAR_ALL, MERGE, LOAD
    }
}
