package com.micle.totemofreviving.network;

import com.micle.totemofreviving.TotemOfReviving;
import com.micle.totemofreviving.items.TotemOfRevivingItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SRequestTotemTarget {
    private final UUID player_uuid;
    private final Hand hand;

    public C2SRequestTotemTarget(final UUID player_uuid, final Hand hand) {
        this.player_uuid = player_uuid;
        this.hand = hand;
    }

    public static void encode(final C2SRequestTotemTarget msg, final PacketBuffer packet_buffer) {
        packet_buffer.writeUniqueId(msg.player_uuid);
        packet_buffer.writeEnumValue(msg.hand);
    }

    public static C2SRequestTotemTarget decode(final PacketBuffer packet_buffer) {
        return new C2SRequestTotemTarget(packet_buffer.readUniqueId(), packet_buffer.readEnumValue(Hand.class));
    }

    public static void handle(final C2SRequestTotemTarget msg, final Supplier<NetworkEvent.Context> context_supplier) {
        final NetworkEvent.Context context = context_supplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = TotemOfReviving.players.getPlayerByUUID(msg.player_uuid);
            if (sender == null) { return; }

            ItemStack item = sender.getHeldItem(msg.hand);
            int current_player_index = item.getOrCreateTag().getInt(TotemOfRevivingItem.TAG_TARGET_INDEX) + 1;
            if (current_player_index > TotemOfReviving.players.getCurrentPlayerCount()-1) {
                current_player_index = 0;
            }

            item.getOrCreateTag().putInt(TotemOfRevivingItem.TAG_TARGET_INDEX, current_player_index);
            item.getOrCreateTag().putString(TotemOfRevivingItem.TAG_TARGET_NAME, TotemOfReviving.players.getPlayers().get(current_player_index).getDisplayName().getString());
            sender.sendMessage(new StringTextComponent(TextFormatting.GRAY + "Target: " + TextFormatting.DARK_GRAY + item.getOrCreateTag().getString(TotemOfRevivingItem.TAG_TARGET_NAME)), sender.getUniqueID());
        });
        context.setPacketHandled(true);
    }
}
