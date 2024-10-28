package me.desht.modularrouters.item.module;

import com.google.common.collect.ImmutableList;
import me.desht.modularrouters.client.util.ClientUtil;
import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.config.ConfigHolder;
import me.desht.modularrouters.container.ModuleMenu;
import me.desht.modularrouters.core.ModDataComponents;
import me.desht.modularrouters.core.ModMenuTypes;
import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.logic.compiled.CompiledDistributorModule;
import me.desht.modularrouters.logic.compiled.CompiledDistributorModule.DistributorSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class DistributorModule extends SenderModule2 {
    private static final TintColor TINT_COLOR = new TintColor(240, 240, 60);

    public DistributorModule(Properties properties) {
        super(properties.component(ModDataComponents.DISTRIBUTOR_SETTINGS, DistributorSettings.DEFAULT),
                CompiledDistributorModule::new);
    }

    @Override
    public void addSettingsInformation(ItemStack stack, List<Component> list) {
        super.addSettingsInformation(stack, list);

        DistributorSettings settings = stack.getOrDefault(ModDataComponents.DISTRIBUTOR_SETTINGS, DistributorSettings.DEFAULT);
        list.add(ClientUtil.xlate("modularrouters.guiText.tooltip.distributor.strategy").withStyle(ChatFormatting.YELLOW)
                .append(": ").withStyle(ChatFormatting.YELLOW)
                .append(ClientUtil.xlate(settings.strategy().getTranslationKey())).withStyle(ChatFormatting.AQUA));
        list.add(ClientUtil.xlate(settings.direction().getTranslationKey()).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public MenuType<? extends ModuleMenu> getMenuType() {
        return ModMenuTypes.DISTRIBUTOR_MENU.get();
    }

    @Override
    public List<ModuleTarget> getStoredPositions(@Nonnull ItemStack stack) {
        return ImmutableList.copyOf(TargetedModule.getTargets(stack, false));
    }

    @Override
    public TintColor getItemTint() {
        return TINT_COLOR;
    }

    @Override
    protected int getMaxTargets() {
        return 8;
    }

    @Override
    public int getRenderColor(int index) {
        return 0x80B0FF90;
    }

    @Override
    public int getEnergyCost(ItemStack stack) {
        return ConfigHolder.common.energyCosts.distributorModuleEnergyCost.get();
    }
}
