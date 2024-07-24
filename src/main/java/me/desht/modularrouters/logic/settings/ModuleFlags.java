package me.desht.modularrouters.logic.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import me.desht.modularrouters.api.matching.IModuleFlags;
import me.desht.modularrouters.item.module.ModuleItem;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record ModuleFlags(boolean whiteList, boolean matchDamage, boolean matchComponents, boolean matchItemTags, boolean matchAllItems) implements IModuleFlags {
    public static final ModuleFlags DEFAULT = new ModuleFlags(false, true, false, false,false);

    public static final Codec<ModuleFlags> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.BOOL.optionalFieldOf("white_list", false).forGetter(ModuleFlags::whiteList),
            Codec.BOOL.optionalFieldOf("match_damage", false).forGetter(ModuleFlags::matchDamage),
            Codec.BOOL.optionalFieldOf("match_components", false).forGetter(ModuleFlags::matchComponents),
            Codec.BOOL.optionalFieldOf("match_item_tag", false).forGetter(ModuleFlags::matchItemTags),
            Codec.BOOL.optionalFieldOf("match_all", false).forGetter(ModuleFlags::matchAllItems)
    ).apply(builder, ModuleFlags::new));

    public static final StreamCodec<ByteBuf, ModuleFlags> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ModuleFlags decode(ByteBuf buf) {
            byte val = buf.readByte();
            boolean whiteList = (val & 0x01) != 0;
            boolean ignoreDamage = (val & 0x02) != 0;
            boolean ignoreComp = (val & 0x04) != 0;
            boolean ignoreTags = (val & 0x08) != 0;
            boolean matchAll = (val & 0x10) != 0;
            return new ModuleFlags(whiteList, ignoreDamage, ignoreComp, ignoreTags, matchAll);
        }

        @Override
        public void encode(ByteBuf buf, ModuleFlags flags) {
            byte val = 0;
            if (flags.whiteList) val |= 0x01;
            if (flags.matchDamage) val |= 0x02;
            if (flags.matchComponents) val |= 0x04;
            if (flags.matchItemTags) val |= 0x08;
            if (flags.matchAllItems) val |= 0x10;
            buf.writeByte(val);
        }
    };

    public static ModuleFlags forItem(ItemStack stack) {
        return stack.getItem() instanceof ModuleItem ?
                ModuleItem.getCommonSettings(stack).flags() :
                ModuleFlags.DEFAULT;

    }
}
