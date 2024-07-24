package me.desht.modularrouters.integration.ffs;

import dev.ftb.mods.ftbfiltersystem.api.FTBFilterSystemAPI;
import me.desht.modularrouters.api.MRCapabilities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class FFSSetup {
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        Item ftbFilterItem = FTBFilterSystemAPI.api().filterItem();
        if (ftbFilterItem != Items.AIR) {
            event.registerItem(MRCapabilities.ITEM_MATCHER, (stack, ctx) -> FFSItemMatcher::new, ftbFilterItem);
        }
    }
}
