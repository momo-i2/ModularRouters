package me.desht.modularrouters.client.gui;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface IResyncableGui {
    void resync(ItemStack stack);
}
