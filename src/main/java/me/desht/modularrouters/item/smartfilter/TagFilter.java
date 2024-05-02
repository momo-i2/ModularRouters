package me.desht.modularrouters.item.smartfilter;

import me.desht.modularrouters.container.AbstractSmartFilterMenu;
import me.desht.modularrouters.container.TagFilterMenu;
import me.desht.modularrouters.core.ModDataComponents;
import me.desht.modularrouters.logic.filter.matchers.IItemMatcher;
import me.desht.modularrouters.logic.filter.matchers.TagMatcher;
import me.desht.modularrouters.util.MFLocator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TagFilter extends SmartFilterItem {
    private static final int MAX_SIZE = 6;

    public static List<TagKey<Item>> getTagList(ItemStack filterStack) {
        List<String> strings = filterStack.getOrDefault(ModDataComponents.FILTER_STRINGS, List.of());
        return strings.stream()
                .filter(ResourceLocation::isValidResourceLocation)
                .map(s -> TagKey.create(Registries.ITEM, new ResourceLocation(s)))
                .toList();
    }

    public static void setTagList(ItemStack filterStack, List<TagKey<Item>> tags) {
        filterStack.set(ModDataComponents.FILTER_STRINGS, tags.stream().map(t -> t.location().toString()).toList());
    }

    @NotNull
    @Override
    public IItemMatcher compile(ItemStack filterStack, ItemStack moduleStack) {
        return new TagMatcher(getTagList(filterStack));
    }

    @Override
    public void addExtraInformation(ItemStack stack, List<Component> list) {
        super.addExtraInformation(stack, list);

        List<TagKey<Item>> l = getTagList(stack);
        addCountInfo(list, l.size());
        list.addAll(l.stream()
                .map(s -> " • " + ChatFormatting.AQUA + s.location())
                .map(Component::literal)
                .toList());
    }

//    @Nullable
//    @Override
//    public GuiSyncMessage onReceiveSettingsMessage(Player player, FilterSettingsMessage message, ItemStack filterStack, ItemStack moduleStack) {
//        List<TagKey<Item>> tagList;
//        switch (message.op()) {
//            case ADD_STRING -> {
//                tagList = new ArrayList<>(getTagList(filterStack));
//                String t = message.payload().getString("Tag");
//                if (tagList.size() < MAX_SIZE && ResourceLocation.isValidResourceLocation(t)) {
//                    TagKey<Item> tag = TagKey.create(Registries.ITEM, new ResourceLocation(t));
//                    if (!tagList.contains(tag)) {
//                        tagList.add(tag);
//                        setTagList(filterStack, tagList);
//                        return new GuiSyncMessage(filterStack);
//                    }
//                }
//            }
//            case REMOVE_AT -> {
//                int pos = message.payload().getInt("Pos");
//                tagList = new ArrayList<>(getTagList(filterStack));
//                if (pos >= 0 && pos < tagList.size()) {
//                    tagList.remove(pos);
//                    setTagList(filterStack, tagList);
//                    return new GuiSyncMessage(filterStack);
//                }
//            }
//            default -> ModularRouters.LOGGER.warn("received unexpected message type " + message.op() + " for " + filterStack);
//        }
//        return null;
//    }

    @Override
    public int getSize(ItemStack filterStack) {
        return getTagList(filterStack).size();
    }

    @Override
    public AbstractSmartFilterMenu createMenu(int windowId, Inventory invPlayer, MFLocator loc) {
        return new TagFilterMenu(windowId, invPlayer, loc);
    }
}
