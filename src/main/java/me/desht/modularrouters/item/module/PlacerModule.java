package me.desht.modularrouters.item.module;

import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.logic.compiled.CompiledPlacerModule;
import net.minecraft.world.item.ItemStack;

public class PlacerModule extends ModuleItem {
    private static final TintColor TINT_COLOR = new TintColor(240, 208, 208);

    public PlacerModule(Properties properties) {
        super(properties, CompiledPlacerModule::new);
    }

    @Override
    public TintColor getItemTint() {
        return TINT_COLOR;
    }

    @Override
    public int getEnergyCost(ItemStack stack) {
        return ConfigHolder.common.energyCosts.placerModuleEnergyCost.get();
    }
}
