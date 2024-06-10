package me.desht.modularrouters.logic.compiled;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class CompiledCreativeModule extends CompiledModule {
    int pos = 0;

    public CompiledCreativeModule(ModularRouterBlockEntity router, ItemStack stack) {
        super(router, stack);
    }

    @Override
    public boolean execute(@Nonnull ModularRouterBlockEntity router) {
        List<ItemStack> stacks = getFilter().getRawStacks();
        if (stacks.isEmpty()) return false;

        int n = 0;
        while (n++ < stacks.size()) {
            if (pos >= stacks.size()) {
                pos = 0;
            }
            ItemStack stack = stacks.get(pos).copyWithCount(getItemsPerTick(router));
            pos++;
            ItemStack inserted = router.insertBuffer(stack);
            if (inserted.getCount() < stack.getCount()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean shouldStoreRawFilterItems() {
        return true;
    }
}
