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
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ModItems.TOTEM_OF_REVIVING.get())
                .key('#', Items.TOTEM_OF_UNDYING)
                .key('@', Items.DIAMOND)
                .patternLine("@@@")
                .patternLine("@#@")
                .patternLine("@@@")
                .addCriterion("has_item", hasItem(Items.TOTEM_OF_UNDYING))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.REVIVING_CHARGE.get())
                .key('#', Items.TOTEM_OF_UNDYING)
                .key('@', Items.DIAMOND_BLOCK)
                .key('E', Items.ENDER_PEARL)
                .patternLine("@E@")
                .patternLine("E#E")
                .patternLine("@E@")
                .addCriterion("has_item", hasItem(ModItems.TOTEM_OF_REVIVING.get()))
                .build(consumer);
    }
}
