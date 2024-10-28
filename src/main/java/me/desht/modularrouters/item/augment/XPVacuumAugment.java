package me.desht.modularrouters.item.augment;

import me.desht.modularrouters.item.module.ModuleItem;
import me.desht.modularrouters.item.module.VacuumModule;

public class XPVacuumAugment extends AugmentItem {
    public XPVacuumAugment(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAugments(ModuleItem moduleType) {
        return moduleType instanceof VacuumModule ? 1 : 0;
    }
}
