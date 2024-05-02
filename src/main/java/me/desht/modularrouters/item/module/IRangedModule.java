package me.desht.modularrouters.item.module;

import net.minecraft.world.item.ItemStack;

public interface IRangedModule {
    int getBaseRange();
    int getHardMaxRange();

    default int getCurrentRange(ItemStack stack) {
        return Math.max(1, Math.min(getHardMaxRange(), getBaseRange() + ModuleItem.getRangeModifier(stack)));
    }

    default int getCurrentRange(int boost) { // yay java 8
        return Math.max(1, Math.min(getHardMaxRange(), getBaseRange() + boost));
    }
}
