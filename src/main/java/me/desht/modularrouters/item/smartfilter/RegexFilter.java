package me.desht.modularrouters.item.smartfilter;

import me.desht.modularrouters.core.ModDataComponents;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.logic.filter.matchers.IItemMatcher;
import me.desht.modularrouters.logic.filter.matchers.RegexMatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RegexFilter extends SmartFilterItem {
    private static final String NBT_REGEX = "Regex";
    public static final int MAX_SIZE = 6;

    public RegexFilter() {
        super(ModItems.defaultProps()
                .component(ModDataComponents.FILTER_STRINGS, List.of()));
    }

    @Override
    public boolean hasMenu() {
        return false;
    }

    public static List<String> getRegexList(ItemStack filterStack) {
        return filterStack.getOrDefault(ModDataComponents.FILTER_STRINGS, List.of());
    }

    public static void setRegexList(ItemStack filterStack, List<String> regexList) {
        filterStack.set(ModDataComponents.FILTER_STRINGS, regexList);
    }

    @Override
    public void addExtraInformation(ItemStack itemstack, List<Component> list) {
        super.addExtraInformation(itemstack, list);

        List<String> regexList = getRegexList(itemstack);
        addCountInfo(list, regexList.size());
        list.addAll(regexList.stream().map(s -> " • " + ChatFormatting.AQUA + "/" + s + "/").map(Component::literal).toList());
    }

    @Override
    public IItemMatcher compile(ItemStack filterStack, ItemStack moduleStack) {
        return new RegexMatcher(getRegexList(filterStack));
    }

//    @Override
//    public GuiSyncMessage onReceiveSettingsMessage(Player player, FilterSettingsMessage message, ItemStack filterStack, ItemStack moduleStack) {
//        List<String> l;
//        switch (message.op()) {
//            case ADD_STRING -> {
//                String regex = message.payload().getString("String");
//                l = getRegexList(filterStack);
//                if (l.size() < MAX_SIZE) {
//                    l.add(regex);
//                    setRegexList(filterStack, l);
//                    return new GuiSyncMessage(filterStack);
//                }
//            }
//            case REMOVE_AT -> {
//                int pos = message.payload().getInt("Pos");
//                l = getRegexList(filterStack);
//                if (pos >= 0 && pos < l.size()) {
//                    l.remove(pos);
//                    setRegexList(filterStack, l);
//                    return new GuiSyncMessage(filterStack);
//                }
//            }
//            default -> ModularRouters.LOGGER.warn("received unexpected message type " + message.op() + " for " + filterStack);
//        }
//        return null;
//    }

    @Override
    public int getSize(ItemStack filterStack) {
        return getRegexList(filterStack).size();
    }
}
