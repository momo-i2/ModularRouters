package me.desht.modularrouters.item.augment;

import me.desht.modularrouters.item.module.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import static me.desht.modularrouters.client.util.ClientUtil.xlate;

public class StackAugment extends AugmentItem {
    public StackAugment(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAugments(ModuleItem moduleType) {
        return moduleType instanceof DetectorModule
                || moduleType instanceof ExtruderModule1 || moduleType instanceof ExtruderModule2
                || moduleType instanceof BreakerModule || moduleType instanceof PlacerModule
                || moduleType instanceof FluidModule1 || moduleType instanceof FluidModule2 ? 0 : 6;
    }

    @Override
    public Component getExtraInfo(int c, ItemStack stack) {
        return Component.literal(" - ").append(xlate("modularrouters.itemText.augments.stackInfo", Math.min(1 << c, 64)));
    }
}
