package me.desht.modularrouters.logic.settings;

import me.desht.modularrouters.util.TranslatableEnum;
import net.minecraft.util.StringRepresentable;

public enum RedstoneBehaviour implements TranslatableEnum, StringRepresentable {
    ALWAYS("always"),
    LOW("low"),
    HIGH("high"),
    NEVER("never"),
    PULSE("pulse");

    private final String name;

    RedstoneBehaviour(String name) {
        this.name = name;
    }

    public static RedstoneBehaviour forValue(String string) {
        try {
            return RedstoneBehaviour.valueOf(string);
        } catch (IllegalArgumentException e) {
            return ALWAYS;
        }
    }

    public boolean shouldRun(boolean powered, boolean pulsed) {
        return switch (this) {
            case ALWAYS -> true;
            case LOW -> !powered;
            case HIGH -> powered;
            case PULSE -> pulsed;
            case NEVER -> false;
        };
    }

    @Override
    public String getTranslationKey() {
        return "modularrouters.guiText.tooltip.redstone." + name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
