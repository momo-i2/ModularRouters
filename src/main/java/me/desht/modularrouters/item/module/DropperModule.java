package me.desht.modularrouters.item.module;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.logic.compiled.CompiledDropperModule;
import me.desht.modularrouters.logic.compiled.CompiledModule;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

public class DropperModule extends ModuleItem {
    private static final TintColor TINT_COLOR = new TintColor(230, 204, 240);

    public DropperModule(Properties properties) {
        super(properties, CompiledDropperModule::new);
    }

    protected DropperModule(Item.Properties properties, BiFunction<ModularRouterBlockEntity, ItemStack, ? extends CompiledModule> compiler) {
        super(properties, compiler);
    }

    @Override
    public TintColor getItemTint() {
        return TINT_COLOR;
    }

    @Override
    public int getEnergyCost(ItemStack stack) {
        return ConfigHolder.common.energyCosts.dropperModuleEnergyCost.get();
    }
}
