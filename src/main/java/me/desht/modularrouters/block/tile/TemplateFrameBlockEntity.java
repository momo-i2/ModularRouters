package me.desht.modularrouters.block.tile;

import me.desht.modularrouters.block.CamouflageableBlock;
import me.desht.modularrouters.core.ModBlockEntities;
import me.desht.modularrouters.util.Scheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.data.ModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class TemplateFrameBlockEntity extends BlockEntity implements ICamouflageable {
    private static final String NBT_CAMO_NAME = "CamouflageName";
    private static final String NBT_MIMIC = "Mimic";

    private BlockState camouflage = null;  // block to masquerade as
    private boolean extendedMimic; // true if extra mimicking is done (light, hardness, blast resistance)

    public TemplateFrameBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEMPLATE_FRAME.get(), pos, state);
    }

    @Override
    public BlockState getCamouflage() {
        return camouflage;
    }

    public void setCamouflage(BlockState camouflage) {
        this.camouflage = camouflage;
        requestModelDataUpdate();
        setChanged();
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        return ModelData.builder()
                .with(CamouflageableBlock.CAMOUFLAGE_STATE, camouflage)
                .build();
    }

    @Override
    public boolean extendedMimic() {
        return extendedMimic;
    }

    public void setExtendedMimic(boolean mimic) {
        this.extendedMimic = mimic;
        setChanged();
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        camouflage = getCamoStateFromNBT(compound, provider);
        extendedMimic = compound.getBoolean(NBT_MIMIC);
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.putBoolean(NBT_MIMIC, extendedMimic);
        if (camouflage != null) {
            compound.put(NBT_CAMO_NAME, NbtUtils.writeBlockState(camouflage));
        }
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
        if (pkt.getTag() != null) {
            camouflage = getCamoStateFromNBT(pkt.getTag(), provider);
            extendedMimic = pkt.getTag().getBoolean("Mimic");
            if (camouflage != null && extendedMimic && camouflage.getLightEmission(getLevel(), getBlockPos()) > 0) {
                Objects.requireNonNull(getLevel()).getChunkSource().getLightEngine().checkBlock(worldPosition);
            }
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        super.handleUpdateTag(tag, provider);

        camouflage = getCamoStateFromNBT(tag, provider);
        extendedMimic = tag.getBoolean("Mimic");
        if (camouflage != null && extendedMimic && camouflage.getLightEmission(getLevel(), getBlockPos()) > 0) {
            // this needs to be deferred a tick because the chunk isn't fully loaded,
            // so any attempt to relight will be ignored
            Scheduler.client().schedule(() -> Objects.requireNonNull(getLevel()).getChunkSource().getLightEngine().checkBlock(worldPosition), 1L);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag compound = new CompoundTag();
        compound.putInt("x", worldPosition.getX());
        compound.putInt("y", worldPosition.getY());
        compound.putInt("z", worldPosition.getZ());
        compound.putBoolean("Mimic", extendedMimic);

        return getNBTFromCamoState(compound, camouflage);
    }

    private BlockState getCamoStateFromNBT(CompoundTag tag, HolderLookup.Provider provider) {
        // level isn't necessarily available here
//        var lookup = level == null ? BuiltInRegistries.BLOCK.asLookup() : level.holderLookup(Registries.BLOCK);
        var lookup = provider.lookup(Registries.BLOCK).orElse(BuiltInRegistries.BLOCK.asLookup());
        if (tag.contains(NBT_CAMO_NAME)) {
            return NbtUtils.readBlockState(lookup, tag.getCompound(NBT_CAMO_NAME));
        }
        return null;
    }

    private static CompoundTag getNBTFromCamoState(CompoundTag compound, BlockState camouflage) {
        if (camouflage != null) {
            compound.put(NBT_CAMO_NAME, NbtUtils.writeBlockState(camouflage));
        }
        return compound;
    }

    public void setCamouflage(ItemStack itemStack, Direction facing, Direction routerFacing) {
        if (itemStack.getItem() instanceof BlockItem b) {
            camouflage = b.getBlock().defaultBlockState();
            if (camouflage.hasProperty(BlockStateProperties.AXIS)) {
                camouflage = camouflage.setValue(BlockStateProperties.AXIS, facing.getAxis());
            } else if (camouflage.hasProperty(BlockStateProperties.FACING)) {
                camouflage = camouflage.setValue(BlockStateProperties.FACING, facing);
            } else if (camouflage.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                camouflage = camouflage.setValue(BlockStateProperties.HORIZONTAL_FACING,
                        facing.getAxis() == Direction.Axis.Y ? routerFacing : facing);
            }
            setChanged();
        }
    }
}
