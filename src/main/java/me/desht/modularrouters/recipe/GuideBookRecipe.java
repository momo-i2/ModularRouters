package me.desht.modularrouters.recipe;

import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.core.ModRecipes;
import me.desht.modularrouters.integration.patchouli.PatchouliHelper;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.neoforged.fml.ModList;

public class GuideBookRecipe extends ShapelessRecipe {
    public GuideBookRecipe(CraftingBookCategory category) {
        super( "modularrouters:guide_book", category, makeGuideBook(),
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.BOOK), Ingredient.of(ModItems.BLANK_MODULE.get()))
        );
    }

    public static ItemStack makeGuideBook() {
        return ModList.get().isLoaded("patchouli") ?
                PatchouliHelper.makePatchouliBook() :
                Util.make(new ItemStack(Items.BOOK),
                        s -> s.set(DataComponents.CUSTOM_NAME, Component.literal("Patchouli is not installed")));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GUIDE_BOOK.get();
    }
}
