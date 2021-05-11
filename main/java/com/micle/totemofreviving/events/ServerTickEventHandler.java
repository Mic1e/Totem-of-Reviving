package com.micle.totemofreviving.events;

import com.micle.totemofreviving.TotemOfReviving;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ServerTickEventHandler {
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        TotemOfReviving.players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
    }
}
