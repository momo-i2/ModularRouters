package me.desht.modularrouters.event;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.item.module.ModuleItem;
import me.desht.modularrouters.network.messages.ValidateModuleMessage;
import me.desht.modularrouters.util.Scheduler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(value = Dist.CLIENT, modid = ModularRouters.MODID)
public class ClientEventHandler {
    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        Scheduler.client().tick();
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel().isClientSide && event.getItemStack().getItem() instanceof ModuleItem) {
            PacketDistributor.sendToServer(new ValidateModuleMessage(event.getHand()));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide && event.getItemStack().getItem() instanceof ModuleItem) {
            PacketDistributor.sendToServer(new ValidateModuleMessage(event.getHand()));
        }
    }
}