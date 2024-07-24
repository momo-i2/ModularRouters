package me.desht.modularrouters.api.matching;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface MatcherProvider {
    /**
     * Provide an implementation of {@code IItemMatcher} which performs item matching.
     * @param stack the itemstack which has the {@link me.desht.modularrouters.api.MRCapabilities#ITEM_MATCHER}
     *              capability
     * @return an item match implementation
     */
    IItemMatcher getMatcher(ItemStack stack);
}
