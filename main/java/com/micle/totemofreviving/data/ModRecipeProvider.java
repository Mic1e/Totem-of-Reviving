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
        ShapedRecipeBuilder.shaped(ModItems.STRAW_TOTEM.get())
                .define('W', Items.WHEAT)
                .define('/', Items.STICK)
                .define('S', Items.STRING)
                .define('N', Items.IRON_NUGGET)
                .pattern("NSN")
                .pattern("NWN")
                .pattern("N/N")
                .unlockedBy("has_item", has(Items.WHEAT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.STRAW_CHARGE.get())
                .define('W', Items.WHEAT)
                .define('E', Items.EMERALD)
                .define('I', Items.IRON_INGOT)
                .pattern("IWI")
                .pattern("WEW")
                .pattern("IWI")
                .unlockedBy("has_item", has(Items.EMERALD))
                .save(consumer);
    }
}
