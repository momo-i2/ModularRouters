package me.desht.modularrouters.integration.patchouli;

import me.desht.modularrouters.ModularRouters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliHelper {
    private static final ResourceLocation BOOK_ID = ResourceLocation.fromNamespaceAndPath(ModularRouters.MODID, "book");

    public static ItemStack makePatchouliBook() {
        return PatchouliAPI.get().getBookStack(BOOK_ID);
    }
}
