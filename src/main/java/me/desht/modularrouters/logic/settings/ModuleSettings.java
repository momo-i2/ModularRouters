package me.desht.modularrouters.logic.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record ModuleSettings(ModuleFlags flags, RelativeDirection facing, ModuleTermination termination,
                             RedstoneBehaviour redstoneBehaviour, int regulatorAmount)
{
    public static final ModuleSettings DEFAULT = new ModuleSettings(
        ModuleFlags.DEFAULT,
        RelativeDirection.NONE,
        ModuleTermination.NONE,
        RedstoneBehaviour.ALWAYS,
        0
    );

    public static final Codec<ModuleSettings> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ModuleFlags.CODEC.fieldOf("flags")
                    .forGetter(ModuleSettings::flags),
            StringRepresentable.fromEnum(RelativeDirection::values).fieldOf("facing")
                    .forGetter(ModuleSettings::facing),
            StringRepresentable.fromEnum(ModuleTermination::values).optionalFieldOf("termination", ModuleTermination.NONE)
                    .forGetter(ModuleSettings::termination),
            StringRepresentable.fromEnum(RedstoneBehaviour::values).optionalFieldOf("redstone", RedstoneBehaviour.ALWAYS)
                    .forGetter(ModuleSettings::redstoneBehaviour),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("regulation", 0)
                    .forGetter(ModuleSettings::regulatorAmount)
    ).apply(builder, ModuleSettings::new));

    public static final StreamCodec<FriendlyByteBuf,ModuleSettings> STREAM_CODEC = StreamCodec.composite(
            ModuleFlags.STREAM_CODEC, ModuleSettings::flags,
            NeoForgeStreamCodecs.enumCodec(RelativeDirection.class), ModuleSettings::facing,
            NeoForgeStreamCodecs.enumCodec(ModuleTermination.class), ModuleSettings::termination,
            NeoForgeStreamCodecs.enumCodec(RedstoneBehaviour.class), ModuleSettings::redstoneBehaviour,
            ByteBufCodecs.VAR_INT, ModuleSettings::regulatorAmount,
            ModuleSettings::new
    );
}
