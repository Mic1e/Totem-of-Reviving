package com.micle.totemofreviving.data.client;

import com.google.gson.JsonElement;
import com.micle.totemofreviving.TotemOfReviving;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existing_file_helper) {
        super(generator, TotemOfReviving.MOD_ID, existing_file_helper);
    }

    @Override
    protected void registerModels() {
        ModelFile item_generated = getExistingFile(mcLoc("item/generated"));

        builder(item_generated, "totem_of_reviving");
        builder(item_generated, "reviving_charge");
        builder(item_generated, "straw_totem");
        builder(item_generated, "straw_charge");
    }

    private ItemModelBuilder builder(ModelFile item_generated, String name) {
        return getBuilder(name).parent(item_generated).texture("layer0", "item/" + name);
    }
}
