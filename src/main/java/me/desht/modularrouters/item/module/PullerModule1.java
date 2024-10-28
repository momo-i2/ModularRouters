package me.desht.modularrouters.item.module;

import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.logic.compiled.CompiledPullerModule1;
import net.minecraft.world.item.ItemStack;

public class PullerModule1 extends ModuleItem {
    private static final TintColor TINT_COLOR = new TintColor(192, 192, 255);

    public PullerModule1(Properties properties) {
        super(properties, CompiledPullerModule1::new);
    }

    @Override
    public TintColor getItemTint() {
        return TINT_COLOR;
    }

    @Override
    public int getEnergyCost(ItemStack stack) {
        return ConfigHolder.common.energyCosts.pullerModule1EnergyCost.get();
    }
}
