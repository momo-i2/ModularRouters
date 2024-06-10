package me.desht.modularrouters.logic.compiled;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.item.module.TargetedModule;
import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.util.BeamData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CompiledPullerModule2 extends CompiledPullerModule1 {
    public CompiledPullerModule2(ModularRouterBlockEntity router, ItemStack stack) {
        super(router, stack);
    }

    @Override
    protected List<ModuleTarget> setupTargets(ModularRouterBlockEntity router, ItemStack stack) {
        ModuleTarget target = TargetedModule.getTarget(stack, !router.nonNullLevel().isClientSide);
        return target == null ? List.of() : List.of(target);
    }

    @Override
    boolean validateRange(ModularRouterBlockEntity router, ModuleTarget target) {
        return target != null
                && target.isSameWorld(router.getLevel())
                && router.getBlockPos().distSqr(target.gPos.pos()) <= getRangeSquared();
    }

    @Override
    protected void playParticles(ModularRouterBlockEntity router, BlockPos targetPos, ItemStack stack) {
        if (router.getUpgradeCount(ModItems.MUFFLER_UPGRADE.get()) < 2) {
            router.addItemBeam(new BeamData.Builder(router, targetPos, 0x6080FF).reversed(true).withItemStack(stack).build());
        }
    }
}
