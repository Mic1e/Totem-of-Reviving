package com.micle.totemofreviving.network;

import com.micle.totemofreviving.TotemOfReviving;
import com.micle.totemofreviving.items.StrawTotemItem;
import com.micle.totemofreviving.items.TotemOfRevivingItem;
import com.micle.totemofreviving.utils.Utils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SRequestTotemCharge {
    private final UUID player_uuid;
    private final Hand hand;
    private final Hand item_charge_hand;

    public C2SRequestTotemCharge(final UUID player_uuid, final Hand hand, final Hand item_charge_hand) {
        this.player_uuid = player_uuid;
        this.hand = hand;
        this.item_charge_hand = item_charge_hand;
    }

    public static void encode(final C2SRequestTotemCharge msg, final PacketBuffer packet_buffer) {
        packet_buffer.writeUniqueId(msg.player_uuid);
        packet_buffer.writeEnumValue(msg.hand);
        packet_buffer.writeEnumValue(msg.item_charge_hand);
    }

    public static C2SRequestTotemCharge decode(final PacketBuffer packet_buffer) {
        return new C2SRequestTotemCharge(packet_buffer.readUniqueId(), packet_buffer.readEnumValue(Hand.class), packet_buffer.readEnumValue(Hand.class));
    }

    public static void handle(final C2SRequestTotemCharge msg, final Supplier<NetworkEvent.Context> context_supplier) {
        final NetworkEvent.Context context = context_supplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = TotemOfReviving.players.getPlayerByUUID(msg.player_uuid);
            if (sender == null) { return; }

            ItemStack charge_item = sender.getHeldItem(msg.item_charge_hand);
            ItemStack totem_item = sender.getHeldItem(msg.hand);
            charge_item.setCount(charge_item.getCount()-1);
            totem_item.getOrCreateTag().putInt(TotemOfRevivingItem.TAG_CHARGE_AMOUNT, totem_item.getOrCreateTag().getInt(TotemOfRevivingItem.TAG_CHARGE_AMOUNT)+1);

            if (totem_item.getOrCreateTag().contains(StrawTotemItem.TAG_FAIL_CHANCE)) {
                int fail_chance = totem_item.getOrCreateTag().getInt(StrawTotemItem.TAG_FAIL_CHANCE);
                if (Utils.randomIntRange(0, 100) <= fail_chance) {
                    sender.setHeldItem(msg.hand, new ItemStack(Items.AIR));
                    sender.attackEntityFrom(DamageSource.GENERIC, (sender.getHealth() * (fail_chance / 100.0f)));
                } else {
                    totem_item.getOrCreateTag().putInt(StrawTotemItem.TAG_FAIL_CHANCE, fail_chance+5);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
