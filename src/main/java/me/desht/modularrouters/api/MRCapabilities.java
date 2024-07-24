package me.desht.modularrouters.api;

import me.desht.modularrouters.api.matching.MatcherProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;

import java.util.Optional;

import static me.desht.modularrouters.util.MiscUtil.RL;

public class MRCapabilities {
    /**
     * Capability which allows any item to function as a filter for Modular Routers. Any item which implements
     * this interface, providing a valid implementation of {@link me.desht.modularrouters.api.matching.IItemMatcher}
     * will function as a filter in the filter slots of a module.
     */
    public static final ItemCapability<MatcherProvider,Void> ITEM_MATCHER
            = ItemCapability.createVoid(RL("item_matcher"), MatcherProvider.class);

    public static Optional<MatcherProvider> getMatcherProvider(ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(ITEM_MATCHER));
    }
}
