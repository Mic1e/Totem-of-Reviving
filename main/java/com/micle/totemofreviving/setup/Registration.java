package com.micle.totemofreviving.setup;

import com.micle.totemofreviving.TotemOfReviving;
import com.micle.totemofreviving.events.ServerTickEventHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TotemOfReviving.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TotemOfReviving.MOD_ID);

    public static void register() {
        IEventBus mod_event_bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(mod_event_bus);
        ITEMS.register(mod_event_bus);
        MinecraftForge.EVENT_BUS.register(new ServerTickEventHandler());

        ModItems.register();
    }
}
