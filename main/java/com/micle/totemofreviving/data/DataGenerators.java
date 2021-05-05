package com.micle.totemofreviving.data;

import com.micle.totemofreviving.TotemOfReviving;
import com.micle.totemofreviving.data.client.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TotemOfReviving.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent e) {
        DataGenerator gen = e.getGenerator();
        ExistingFileHelper existing_file_helper = e.getExistingFileHelper();

        gen.addProvider(new ModItemModelProvider(gen, existing_file_helper));

        gen.addProvider(new ModRecipeProvider(gen));
    }
}
