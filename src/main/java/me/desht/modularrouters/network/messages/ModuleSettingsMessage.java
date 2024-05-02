package me.desht.modularrouters.network.messages;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.container.ModuleMenu;
import me.desht.modularrouters.item.module.ModuleItem;
import me.desht.modularrouters.util.MFLocator;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Received on: SERVER
 * <p>
 * Sent by client when a player updates a module's settings via its GUI.
 */
public record ModuleSettingsMessage(MFLocator locator, ItemStack newStack) implements CustomPacketPayload {
    public static final Type<ModuleSettingsMessage> TYPE = new Type<>(MiscUtil.RL("module_settings"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ModuleSettingsMessage> STREAM_CODEC = StreamCodec.composite(
            MFLocator.STREAM_CODEC, ModuleSettingsMessage::locator,
            ItemStack.STREAM_CODEC, ModuleSettingsMessage::newStack,
            ModuleSettingsMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(ModuleSettingsMessage message, IPayloadContext context) {
        Player player = context.player();
        if (!(player.containerMenu instanceof ModuleMenu)) {
            ModularRouters.LOGGER.warn("ignoring ModuleSettingsMessage for {} - player does not have a module GUI open", player.getGameProfile().getName());
            return;
        }

        MFLocator locator = message.locator();
        ItemStack newStack = message.newStack();
        ItemStack moduleStack = locator.getModuleStack(player);

        if (moduleStack.getItem() instanceof ModuleItem && newStack.getItem() == moduleStack.getItem()) {
            locator.setModuleStack(player, newStack);
        } else {
            ModularRouters.LOGGER.warn("ignoring ModuleSettingsMessage for {} - expected module not found @ {}", player.getGameProfile().getName(), locator);
        }
    }
}
