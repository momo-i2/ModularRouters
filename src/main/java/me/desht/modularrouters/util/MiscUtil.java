package me.desht.modularrouters.util;

import me.desht.modularrouters.ModularRouters;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class MiscUtil {
    public static Direction[] DIRECTIONS = new Direction[] {
            // same as Direction.VALUES but that's private
            Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private static final int WRAP_LENGTH = 45;

    public static void appendMultilineText(List<Component> result, ChatFormatting formatting, String key, Object... args) {
        for (String s : I18n.get(key, args).split(Pattern.quote("${br}"))) {
            result.add(new TextComponent(s).withStyle(formatting));
        }
    }

    public static MutableComponent asFormattable(Component component) {
        return component instanceof MutableComponent ? (MutableComponent) component : component.plainCopy();
    }

    public static List<String> wrapString(String text) {
        return wrapString(text, WRAP_LENGTH);
    }

    public static List<TextComponent> wrapStringAsTextComponent(String text) {
        return wrapString(text, WRAP_LENGTH).stream().map(TextComponent::new).toList();
    }

    public static String commify(int n) {
        return NumberFormat.getNumberInstance().format(n);
    }

    public static List<String> wrapString(String text, int maxCharPerLine) {
        List<String> result = new ArrayList<>();

        StringBuilder builder = new StringBuilder(text.length());
        String format = "";
        for (String para : text.split(Pattern.quote("${br}"))) {
            StringTokenizer tok = new StringTokenizer(para, " ");
            int lineLen = 0;
            while (tok.hasMoreTokens()) {
                String word = tok.nextToken();
                int idx = word.lastIndexOf("\u00a7");
                if (idx >= 0 && idx < word.length() - 1) {
                    // note the formatting sequence so we can apply to next line if any
                    format = word.substring(idx, idx + 2);
                    // formatting sequence does not contribute to line length
                    lineLen -= 2;
                }
                if (lineLen + word.length() > maxCharPerLine) {
                    result.add(builder.toString());
                    builder.delete(0, builder.length());
                    builder.append(format);
                    lineLen = 0;
                } else if (lineLen > 0) {
                    builder.append(" ");
                    lineLen++;
                }
                builder.append(word);
                lineLen += word.length();
            }
            result.add(builder.toString());
            builder.delete(0, builder.length());
            builder.append(format);
        }
        return result;
    }

    public static String locToString(ResourceLocation dim, BlockPos pos) {
        String s = dim.getNamespace().equals("minecraft") ? dim.getPath() : dim.toString();
        return String.format("%s [%d,%d,%d]", s, pos.getX(), pos.getY(), pos.getZ());
    }

    public static String locToString(GlobalPos pos) {
        return locToString(pos.dimension().location(), pos.pos());
    }

    public static ResourceLocation RL(String name) {
        return new ResourceLocation(ModularRouters.MODID, name);
    }

    public static int getYawFromFacing(Direction facing) {
        return switch (facing) {
            case NORTH -> 180;
            case WEST -> 90;
            case EAST -> -90;
            default -> 0; // including SOUTH
        };
    }

    public static Component settingsStr(String prefix, Component c) {
        return new TextComponent(prefix).append(c);  // appendSibling
    }

    public static CompoundTag serializeGlobalPos(GlobalPos globalPos) {
        CompoundTag tag = new CompoundTag();
        tag.put("pos", net.minecraft.nbt.NbtUtils.writeBlockPos(globalPos.pos()));
        tag.putString("dim", globalPos.dimension().location().toString());
        return tag;
    }

    public static GlobalPos deserializeGlobalPos(CompoundTag tag) {
        ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString("dim")));
        return GlobalPos.of(worldKey, NbtUtils.readBlockPos(tag.getCompound("pos")));
    }

    public static ServerLevel getWorldForGlobalPos(GlobalPos pos) {
        return ServerLifecycleHooks.getCurrentServer().getLevel(pos.dimension());
    }

    public static GlobalPos makeGlobalPos(Level w, BlockPos pos) {
        return GlobalPos.of(w.dimension(), pos);
    }

    // this method from Block went missing in 1.16.2
    public static boolean blockHasSolidSide(BlockState state, BlockGetter worldIn, BlockPos pos, Direction side) {
        return Block.isFaceFull(state.getBlockSupportShape(worldIn, pos), side);
    }
}
