package me.desht.modularrouters.logic.filter.matchers;

import me.desht.modularrouters.logic.settings.ModuleFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public interface IItemMatcher {
    boolean matchItem(ItemStack stack, ModuleFlags flags);

    default boolean matchFluid(Fluid fluid, ModuleFlags flags) {
        return false;
    }
}
