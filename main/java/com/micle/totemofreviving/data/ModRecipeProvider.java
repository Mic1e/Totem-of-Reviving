package com.micle.totemofreviving.data;

import com.micle.totemofreviving.setup.ModItems;
import net.minecraft.data.*;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator_in) {
        super(generator_in);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModItems.TOTEM_OF_REVIVING.get())
                .define('#', Items.TOTEM_OF_UNDYING)
                .define('@', Items.DIAMOND)
                .pattern("@@@")
                .pattern("@#@")
                .pattern("@@@")
                .unlockedBy("has_item", has(Items.TOTEM_OF_UNDYING))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.REVIVING_CHARGE.get())
                .define('#', Items.TOTEM_OF_UNDYING)
                .define('@', Items.DIAMOND_BLOCK)
                .define('E', Items.ENDER_PEARL)
                .pattern("@E@")
                .pattern("E#E")
                .pattern("@E@")
                .unlockedBy("has_item", has(ModItems.TOTEM_OF_REVIVING.get()))
                .save(consumer);
    }
}
