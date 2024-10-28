package me.desht.modularrouters.item.module;

import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.logic.compiled.CompiledEnergyOutputModule;
import net.minecraft.world.item.ItemStack;

public class EnergyOutputModule extends ModuleItem {
    private static final TintColor TINT_COLOR = new TintColor(106, 12, 120);

    public EnergyOutputModule(Properties properties) {
        super(properties, CompiledEnergyOutputModule::new);
    }

    @Override
    public TintColor getItemTint() {
        return TINT_COLOR;
    }

    @Override
    public int getEnergyCost(ItemStack stack) {
        return ConfigHolder.common.energyCosts.energyoutputModuleEnergyCost.get();
    }

}
