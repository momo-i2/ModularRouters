package me.desht.modularrouters.core;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.recipe.GuideBookRecipe;
import me.desht.modularrouters.recipe.PickaxeModuleRecipe.BreakerModuleRecipe;
import me.desht.modularrouters.recipe.PickaxeModuleRecipe.ExtruderModule1Recipe;
import me.desht.modularrouters.recipe.ResetModuleRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModularRouters.MODID);

    public static final Supplier<CustomRecipe.Serializer<BreakerModuleRecipe>> BREAKER_MODULE
            = RECIPES.register("breaker_module", () -> new CustomRecipe.Serializer<>(BreakerModuleRecipe::new));
    public static final Supplier<CustomRecipe.Serializer<ExtruderModule1Recipe>> EXTRUDER_MODULE_1
            = RECIPES.register("extruder_module_1", () -> new CustomRecipe.Serializer<>(ExtruderModule1Recipe::new));

    public static final Supplier<CustomRecipe.Serializer<ResetModuleRecipe>> MODULE_RESET
            = RECIPES.register("module_reset", () -> new CustomRecipe.Serializer<>(ResetModuleRecipe::new));
    public static final Supplier<CustomRecipe.Serializer<GuideBookRecipe>> GUIDE_BOOK
            = RECIPES.register("guide_book", () -> new CustomRecipe.Serializer<>(GuideBookRecipe::new));
}
