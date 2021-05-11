package com.micle.totemofreviving.network;

import com.micle.totemofreviving.TotemOfReviving;
import com.micle.totemofreviving.items.TotemOfRevivingItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SRequestPlayerRevive {
    private final UUID player_uuid;
    private final Hand hand;

    public C2SRequestPlayerRevive(final UUID player_uuid, final Hand hand) {
        this.player_uuid = player_uuid;
        this.hand = hand;
    }

    public static void encode(final C2SRequestPlayerRevive msg, final PacketBuffer packet_buffer) {
        packet_buffer.writeUniqueId(msg.player_uuid);
        packet_buffer.writeEnumValue(msg.hand);
    }

    public static C2SRequestPlayerRevive decode(final PacketBuffer packet_buffer) {
        return new C2SRequestPlayerRevive(packet_buffer.readUniqueId(), packet_buffer.readEnumValue(Hand.class));
    }

    public static void handle(final C2SRequestPlayerRevive msg, final Supplier<NetworkEvent.Context> context_supplier) {
        final NetworkEvent.Context context = context_supplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = TotemOfReviving.players.getPlayerByUUID(msg.player_uuid);
            if (sender == null) { return; }

            ItemStack item = sender.getHeldItem(msg.hand);
            if (item.getOrCreateTag().getInt(TotemOfRevivingItem.TAG_TARGET_INDEX) > TotemOfReviving.players.getCurrentPlayerCount()-1) {
                sender.sendMessage(new StringTextComponent(TextFormatting.RED + "Error getting target! (Try selecting the target again)"), sender.getUniqueID());
            } else {
                ServerPlayerEntity player_to_revive = TotemOfReviving.players.getPlayerByUsername(item.getOrCreateTag().getString(TotemOfRevivingItem.TAG_TARGET_NAME));
                ServerWorld player_to_revive_world = player_to_revive.getServerWorld();
                ServerWorld sender_world = sender.getServerWorld();
                int required_charge = player_to_revive.getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS));
                if (player_to_revive.isSpectator()) {
                    if (player_to_revive_world.equals(sender_world)) {
                        if (item.getOrCreateTag().getInt(TotemOfRevivingItem.TAG_CHARGE_AMOUNT) >= required_charge) {
                            player_to_revive.teleportKeepLoaded(sender.getPosX(), sender.getPosY(), sender.getPosZ());
                            player_to_revive.setGameType(GameType.SURVIVAL);
                            item.getOrCreateTag().putInt(TotemOfRevivingItem.TAG_CHARGE_AMOUNT, item.getOrCreateTag().getInt(TotemOfRevivingItem.TAG_CHARGE_AMOUNT) - required_charge);
                            sender.sendMessage(new StringTextComponent(TextFormatting.AQUA + "Successfully revived " + TextFormatting.BLUE + player_to_revive.getDisplayName().getString()), sender.getUniqueID());
                        } else {
                            sender.sendMessage(new StringTextComponent(TextFormatting.AQUA + "Not enough charge! Required charge is: " + TextFormatting.BLUE + required_charge), sender.getUniqueID());
                        }
                    } else {
                        sender.sendMessage(new StringTextComponent(TextFormatting.BLUE + player_to_revive.getDisplayName().getString() + TextFormatting.AQUA + " is not in this dimension!"), sender.getUniqueID());
                    }
                } else {
                    sender.sendMessage(new StringTextComponent(TextFormatting.BLUE + player_to_revive.getDisplayName().getString() + TextFormatting.AQUA + " is not dead!"), sender.getUniqueID());
                }
            }
        });
        context.setPacketHandled(true);
    }
}
