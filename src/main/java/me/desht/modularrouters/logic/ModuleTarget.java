package me.desht.modularrouters.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.desht.modularrouters.client.util.ClientUtil;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * Represents a target for a given targeted module, including the dimension, blockpos
 *  and face of the block where insertion/extraction will occur.
 * Targeted modules store one or more of these objects; see also {@link ModuleTargetList}
 */
public class ModuleTarget {
    public final GlobalPos gPos;
    public final Direction face;
    public final String blockTranslationKey;

    private BlockCapabilityCache<IItemHandler,Direction> itemCapCache;
    private BlockCapabilityCache<IFluidHandler,Direction> fluidCapCache;
    private BlockCapabilityCache<IEnergyStorage,Direction> energyCapCache;
    private final Map<BlockCapability<?, ?>, BlockCapabilityCache<?, ?>> capabilityCache = new IdentityHashMap<>();

    public static final Codec<ModuleTarget> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            GlobalPos.CODEC.fieldOf("pos").forGetter(t -> t.gPos),
            Direction.CODEC.fieldOf("face").forGetter(t -> t.face),
            Codec.STRING.optionalFieldOf("key", "").forGetter(t -> t.blockTranslationKey)
    ).apply(builder, ModuleTarget::new));

    public static final StreamCodec<FriendlyByteBuf,ModuleTarget> STREAM_CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC, t -> t.gPos,
            Direction.STREAM_CODEC, t -> t.face,
            ByteBufCodecs.STRING_UTF8, t -> t.blockTranslationKey,
            ModuleTarget::new
    );

    public ModuleTarget(GlobalPos gPos, Direction face, String blockTranslationKey) {
        this.gPos = gPos;
        this.face = face;
        this.blockTranslationKey = blockTranslationKey;
    }

    public ModuleTarget(GlobalPos gPos, Direction face) {
        this(gPos, face, "");
    }

    public ModuleTarget(GlobalPos gPos) {
        this(gPos, null);
    }

    public boolean isSameWorld(@Nullable Level world) {
        return world != null && gPos.dimension() == world.dimension();
    }

    public boolean isSameWorld(ModuleTarget dst) {
        return gPos.dimension() == dst.gPos.dimension();
    }

    /**
     * Check for existence of an item handler client-side.  The target dimension must be the same as the client's
     * current dimension.
     *
     * @return a (lazy optional) item handler
     */
    public boolean hasItemHandlerClientSide() {
        return ClientUtil.theClientLevel().getCapability(Capabilities.ItemHandler.BLOCK, gPos.pos(), face) != null;
    }

    /**
     * Get an item handler for the module target.  Only call this server-side.
     *
     * @return an optional item handler
     */
    public Optional<IItemHandler> getItemHandler() {
        if (itemCapCache == null) {
            ServerLevel level = MiscUtil.getWorldForGlobalPos(gPos);
            if (level == null) {
                return Optional.empty();
            }
            itemCapCache = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, level, gPos.pos(), face);
        }

        return Optional.ofNullable(itemCapCache.getCapability());
    }

    /**
     * Try to get a fluid handler for this module target.  Only call this server-side.
     *
     * @return an optional fluid handler
     */
    public Optional<IFluidHandler> getFluidHandler() {
        if (fluidCapCache == null) {
            ServerLevel level = MiscUtil.getWorldForGlobalPos(gPos);
            if (level == null) {
                return Optional.empty();
            }
            fluidCapCache = BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, level, gPos.pos(), face);
        }
        return Optional.ofNullable(fluidCapCache.getCapability());
    }

    /**
     * Try to get an energy handler for this module target.  Only call this server-side.
     *
     * @return an optional energy handler
     */
    public Optional<IEnergyStorage> getEnergyHandler() {
        if (energyCapCache == null) {
            ServerLevel level = MiscUtil.getWorldForGlobalPos(gPos);
            if (level == null) {
                return Optional.empty();
            }
            energyCapCache = BlockCapabilityCache.create(Capabilities.EnergyStorage.BLOCK, level, gPos.pos(), face);
        }
        return Optional.ofNullable(energyCapCache.getCapability());
    }

    /**
     * Get a cached capability of the module target.
     *
     * @param capability the capability
     * @param context    the capability context
     * @return the capability
     */
    @Nullable
    public <T, C> T getCapability(BlockCapability<T, C> capability, @Nullable C context) {
        var cached = (BlockCapabilityCache<T, C>)capabilityCache.get(capability);
        if (cached != null && Objects.equals(cached.context(), context)) {
            return cached.getCapability();
        }
        ServerLevel level = MiscUtil.getWorldForGlobalPos(gPos);
        if (level == null) {
            return null;
        }
        cached = BlockCapabilityCache.create(capability, level, gPos.pos(), context);
        capabilityCache.put(capability, cached);
        return cached.getCapability();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleTarget that)) return false;
        return gPos.equals(that.gPos) && face == that.face;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gPos, face);
    }

    @Override
    public String toString() {
        return MiscUtil.locToString(gPos) + " " + face;
    }

    public Component getTextComponent() {
        return Component.translatable(blockTranslationKey).withStyle(ChatFormatting.WHITE)
                .append(Component.literal(" @ " + this).withStyle(ChatFormatting.AQUA));
    }
}
