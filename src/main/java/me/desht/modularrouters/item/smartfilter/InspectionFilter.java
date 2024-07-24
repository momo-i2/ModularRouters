package me.desht.modularrouters.item.smartfilter;

import me.desht.modularrouters.core.ModDataComponents;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.api.matching.IItemMatcher;
import me.desht.modularrouters.logic.filter.matchers.InspectionMatcher;
import me.desht.modularrouters.logic.filter.matchers.InspectionMatcher.ComparisonList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.desht.modularrouters.client.util.ClientUtil.xlate;

public class InspectionFilter extends SmartFilterItem {
    public static final int MAX_SIZE = 6;

    public InspectionFilter() {
        super(ModItems.defaultProps()
                .component(ModDataComponents.COMPARISON_LIST, ComparisonList.DEFAULT)
        );
    }

    @Override
    public @NotNull IItemMatcher compile(ItemStack filterStack, ItemStack moduleStack) {
        return new InspectionMatcher(getComparisonList(filterStack));
    }

    @Override
    public boolean hasMenu() {
        return false;
    }

    @Override
    public void addExtraInformation(ItemStack itemstack, List<Component> list) {
        super.addExtraInformation(itemstack, list);
        ComparisonList comparisonList = getComparisonList(itemstack);
        if (!comparisonList.isEmpty()) {
            list.add(xlate("modularrouters.guiText.label.matchAll." + comparisonList.matchAll()).append(":").withStyle(ChatFormatting.YELLOW));
            comparisonList.items().stream()
                    .map(c -> Component.literal("• ").append(c.asLocalizedText().withStyle(ChatFormatting.AQUA)))
                    .forEach(list::add);
        }
    }

    public static ComparisonList getComparisonList(ItemStack filterStack) {
        return filterStack.get(ModDataComponents.COMPARISON_LIST.get());
    }

    public static void setComparisonList(ItemStack filterStack, ComparisonList comparisonList) {
        filterStack.set(ModDataComponents.COMPARISON_LIST.get(), comparisonList);
    }

    @Override
    public int getSize(ItemStack filterStack) {
        return getComparisonList(filterStack).items().size();
    }
}
