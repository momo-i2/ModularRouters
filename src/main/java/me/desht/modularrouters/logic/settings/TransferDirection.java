package me.desht.modularrouters.logic.settings;

import me.desht.modularrouters.util.TranslatableEnum;
import net.minecraft.util.StringRepresentable;

public enum TransferDirection implements TranslatableEnum, StringRepresentable {
    TO_ROUTER("to_router"),
    FROM_ROUTER("from_router");

    private final String name;

    TransferDirection(String name) {
        this.name = name;
    }

    @Override
    public String getTranslationKey() {
        return "modularrouters.itemText.transfer_direction." + name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public String getSymbol() { return this == FROM_ROUTER ? "⟹" : "⟸"; }
}
