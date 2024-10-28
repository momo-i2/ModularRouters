package me.desht.modularrouters.logic.compiled;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.util.BeamData;
import me.desht.modularrouters.util.BlockUtil;
import me.desht.modularrouters.util.InventoryUtils;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class CompiledSenderModule1 extends CompiledModule {
    public CompiledSenderModule1(ModularRouterBlockEntity router, ItemStack stack) {
        super(router, stack);
    }

    @Override
    public boolean execute(@Nonnull ModularRouterBlockEntity router) {
        IItemHandler buffer = router.getBuffer();
        ItemStack bufferStack = buffer.getStackInSlot(0);
        if (getFilter().test(bufferStack)) {
            PositionedItemHandler positionedItemHandler = findTargetInventory(router);
            if (positionedItemHandler.isValid()) {
                int nToSend = getItemsPerTick(router);
                if (getRegulationAmount() > 0) {
                    int existing = InventoryUtils.countItems(bufferStack, positionedItemHandler.handler, getRegulationAmount(), !getFilter().getFlags().matchDamage());
                    nToSend = Math.min(nToSend, getRegulationAmount() - existing);
                    if (nToSend <= 0) {
                        return false;
                    }
                }
                int sent = InventoryUtils.transferItems(buffer, positionedItemHandler.handler, 0, nToSend);
                if (sent > 0) {
                    if (ConfigHolder.common.module.senderParticles.get()) {
                        playParticles(router, positionedItemHandler.pos, bufferStack.copyWithCount(sent));
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    void playParticles(ModularRouterBlockEntity router, BlockPos targetPos, ItemStack stack) {
        if (router.getUpgradeCount(ModItems.MUFFLER_UPGRADE.get()) < 2) {
            router.addItemBeam(new BeamData.Builder(router, targetPos, getBeamColor()).withItemStack(stack).build());
        }
    }

    protected int getBeamColor() {
        return 0xFFC000;
    }

    protected PositionedItemHandler findTargetInventory(ModularRouterBlockEntity router) {
        ModuleTarget target = getEffectiveTarget(router);
        if (target != null) {
            return target.getItemHandler().map(h -> new PositionedItemHandler(target.gPos.pos(), h)).orElse(PositionedItemHandler.INVALID);
        }
        return PositionedItemHandler.INVALID;
    }

    @Override
    public ModuleTarget getEffectiveTarget(ModularRouterBlockEntity router) {
        if (getAbsoluteFacing() != null) {
            BlockPos.MutableBlockPos pos = getTarget().gPos.pos().mutable();
            Direction face = getTarget().face;
            Level level = router.nonNullLevel();
            for (int i = 1; i <= getRange(); i++) {
                if (level.getCapability(Capabilities.ItemHandler.BLOCK, pos, getTarget().face) != null) {
                    GlobalPos gPos = MiscUtil.makeGlobalPos(level, pos.immutable());
                    return new ModuleTarget(gPos, face, BlockUtil.getBlockName(level, pos));
                } else if (!isPassable(level, pos, face)) {
                    return null;
                }
                pos.move(getAbsoluteFacing());
            }
        }
        return null;
    }

    private boolean isPassable(Level w, BlockPos pos, Direction face) {
        BlockState state = w.getBlockState(pos);
        return !MiscUtil.blockHasSolidSide(state, w, pos, face.getOpposite()) || !state.isSolidRender();
    }

    public record PositionedItemHandler(BlockPos pos, IItemHandler handler) {
        static final PositionedItemHandler INVALID = new PositionedItemHandler(null, null);

        boolean isValid() {
            return pos != null && handler != null;
        }
    }
}
