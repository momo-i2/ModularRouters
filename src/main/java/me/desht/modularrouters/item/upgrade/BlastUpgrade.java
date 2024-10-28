package me.desht.modularrouters.item.upgrade;

import me.desht.modularrouters.client.util.TintColor;

public class BlastUpgrade extends UpgradeItem {
    public BlastUpgrade(Properties properties) {
        super(properties);
    }

    @Override
    public TintColor getItemTint() {
        return new TintColor(144, 0, 0);
    }
}
