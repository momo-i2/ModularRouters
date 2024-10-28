package me.desht.modularrouters.item.module;

import com.google.common.collect.ImmutableList;
import me.desht.modularrouters.client.render.area.IPositionProvider;
import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.logic.compiled.CompiledEnergyDistributorModule;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.capabilities.Capabilities;

import javax.annotation.Nonnull;
import java.util.List;

public class EnergyDistributorModule extends TargetedModule implements IRangedModule, IPositionProvider {
    private static final TintColor TINT_COLOR = new TintColor(79, 9, 90);

    public EnergyDistributorModule(Properties properties) {
        super(properties, CompiledEnergyDistributorModule::new);
    }

    @Override
    public TintColor getItemTint() {
        return TINT_COLOR;
    }

    @Override
    public int getEnergyCost(ItemStack stack) {
        return ConfigHolder.common.energyCosts.energydistributorModuleEnergyCost.get();
    }

    @Override
    public int getBaseRange() {
        return 8;
    }

    @Override
    public int getHardMaxRange() {
        return 48;
    }

    @Override
    public List<ModuleTarget> getStoredPositions(@Nonnull ItemStack stack) {
        return ImmutableList.copyOf(TargetedModule.getTargets(stack, false));
    }

    @Override
    protected boolean isValidTarget(UseOnContext ctx) {
        return ctx.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, ctx.getClickedPos(), ctx.getClickedFace()) != null;
    }

    @Override
    protected int getMaxTargets() {
        return 8;
    }

    @Override
    public int getRenderColor(int index) {
        return 0x80E08080;
    }
}
