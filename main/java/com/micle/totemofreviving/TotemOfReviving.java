package com.micle.totemofreviving;

import com.micle.totemofreviving.network.C2SRequestPlayerRevive;
import com.micle.totemofreviving.network.C2SRequestTotemCharge;
import com.micle.totemofreviving.network.C2SRequestTotemTarget;
import com.micle.totemofreviving.setup.Registration;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(TotemOfReviving.MOD_ID)
public class TotemOfReviving {
    public static final String MOD_ID = "totemofreviving";
    public static PlayerList players;

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TotemOfReviving.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public TotemOfReviving() {
        Registration.register();

        int id = 0;
        INSTANCE.registerMessage(id++,
                C2SRequestPlayerRevive.class,
                C2SRequestPlayerRevive::encode,
                C2SRequestPlayerRevive::decode,
                C2SRequestPlayerRevive::handle
        );
        INSTANCE.registerMessage(id++,
                C2SRequestTotemTarget.class,
                C2SRequestTotemTarget::encode,
                C2SRequestTotemTarget::decode,
                C2SRequestTotemTarget::handle
        );
        INSTANCE.registerMessage(id++,
                C2SRequestTotemCharge.class,
                C2SRequestTotemCharge::encode,
                C2SRequestTotemCharge::decode,
                C2SRequestTotemCharge::handle
        );

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
