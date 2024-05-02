package me.desht.modularrouters.item.module;

import me.desht.modularrouters.client.util.ClientUtil;
import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.container.ModuleMenu;
import me.desht.modularrouters.core.ModDataComponents;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.core.ModMenuTypes;
import me.desht.modularrouters.logic.compiled.CompiledBreakerModule;
import me.desht.modularrouters.logic.compiled.CompiledBreakerModule.BreakerSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.List;

public class BreakerModule extends ModuleItem implements IPickaxeUser {
    private static final TintColor TINT_COLOR = new TintColor(240, 208, 208);

    public BreakerModule() {
        super(ModItems.moduleProps()
                        .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
                        .component(ModDataComponents.BREAKER_SETTINGS, BreakerSettings.DEFAULT),
                CompiledBreakerModule::new);
    }

    @Override
    public TintColor getItemTint() {
        return TINT_COLOR;
    }

    @Override
    public int getEnergyCost(ItemStack stack) {
        return ConfigHolder.common.energyCosts.breakerModuleEnergyCost.get();
    }

    @Override
    protected void addSettingsInformation(ItemStack stack, List<Component> list) {
        super.addSettingsInformation(stack, list);

        BreakerSettings settings = stack.get(ModDataComponents.BREAKER_SETTINGS);
        list.add(ClientUtil.xlate(settings.matchType().getTranslationKey()).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public MenuType<? extends ModuleMenu> getMenuType() {
        return ModMenuTypes.BREAKER_MENU.get();
    }
}
