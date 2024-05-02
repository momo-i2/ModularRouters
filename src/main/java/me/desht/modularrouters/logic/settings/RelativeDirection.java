package me.desht.modularrouters.logic.settings;

import me.desht.modularrouters.util.TranslatableEnum;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum RelativeDirection implements StringRepresentable, TranslatableEnum {
    // Direction relative to the facing of the router this module is installed in
    NONE("none", " "),
    DOWN("down", "▼"),
    UP("up", "▲"),
    LEFT("left", "◀"),
    RIGHT("right", "▶"),
    FRONT("front", "▣"),
    BACK("back", "▤");

    private final String name;
    private final String symbol;

    RelativeDirection(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Direction toAbsolute(Direction current) {
        return switch (this) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case LEFT -> current.getClockWise();
            case BACK -> current.getOpposite();
            case RIGHT -> current.getCounterClockWise();
            default -> current; // including FRONT
        };
    }

    public String getSymbol() {
        return symbol;
    }

    public int getTextureX(boolean toggled) {
        return ordinal() * 32 + (toggled ? 16 : 0);
    }

    public int getTextureY() {
        return 48;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public String getTranslationKey() {
        return "modularrouters.guiText.tooltip.relative_dir." + name;
    }
}
