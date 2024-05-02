package me.desht.modularrouters.logic.settings;

import me.desht.modularrouters.util.TranslatableEnum;
import net.minecraft.util.StringRepresentable;

public enum ModuleTermination implements TranslatableEnum, StringRepresentable {
    NONE("none"),
    RAN("ran"),
    NOT_RAN("not_ran");

    private final String name;

    ModuleTermination(String name) {
        this.name = name;
    }

    @Override
    public String getTranslationKey() {
        return "modularrouters.guiText.tooltip.terminate." + name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
