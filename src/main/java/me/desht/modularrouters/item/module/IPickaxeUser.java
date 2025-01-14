package me.desht.modularrouters.item.module;

import me.desht.modularrouters.core.ModDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.Unbreakable;

import java.util.List;

public interface IPickaxeUser {
    ItemContainerContents DEFAULT_PICK = ItemContainerContents.fromItems(List.of(new ItemStack(Items.STONE_PICKAXE)));

    default ItemStack getPickaxe(ItemStack moduleStack) {
        return moduleStack.getOrDefault(ModDataComponents.PICKAXE, DEFAULT_PICK)
                .copyOne();
    }

    default ItemStack setPickaxe(ItemStack moduleStack, ItemStack pickaxeStack) {
        pickaxeStack.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
        moduleStack.set(ModDataComponents.PICKAXE, ItemContainerContents.fromItems(List.of(pickaxeStack)));
        return moduleStack;
    }
}
