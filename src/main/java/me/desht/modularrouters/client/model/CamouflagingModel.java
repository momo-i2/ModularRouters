package me.desht.modularrouters.client.model;

import me.desht.modularrouters.block.CamouflageableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedOverrides;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CamouflagingModel implements IDynamicBakedModel {
    private final BakedModel baseModel;

    CamouflagingModel(BakedModel baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        if (state == null || !(state.getBlock() instanceof CamouflageableBlock)) {
            return baseModel.getQuads(state, side, rand, modelData, renderType);
        }
        BlockState camoState = modelData.get(CamouflageableBlock.CAMOUFLAGE_STATE);

        if (renderType == null) {
            renderType = RenderType.solid(); // workaround for when this isn't set (digging, etc.)
        }
        if (camoState == null && renderType == RenderType.solid()) {
            // No camo
            return baseModel.getQuads(state, side, rand, modelData, renderType);
        } else if (camoState != null && getRenderTypes(camoState, rand, modelData).contains(renderType)) {
            // Steal camo's model
            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(camoState);
            return model.getQuads(camoState, side, rand, modelData, renderType);
        } else {
            // Not rendering in this layer
            return List.of();
        }
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        BlockState camoState = data.get(CamouflageableBlock.CAMOUFLAGE_STATE);
        return IDynamicBakedModel.super.getRenderTypes(camoState == null ? state : camoState, rand, data);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return baseModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isCustomRenderer() {
        return baseModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseModel.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return baseModel.getTransforms();
    }

    @Override
    public BakedOverrides overrides() {
        return baseModel.overrides();
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    static class RouterModel extends CamouflagingModel {
        RouterModel(BakedModel baseModel) {
            super(baseModel);
        }
    }

    static class TemplateFrameModel extends CamouflagingModel {
        TemplateFrameModel(BakedModel baseModel) {
            super(baseModel);
        }
    }
}
