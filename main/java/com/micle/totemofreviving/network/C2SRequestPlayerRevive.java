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
        packet_buffer.writeUUID(msg.player_uuid);
        packet_buffer.writeEnum(msg.hand);
    }

    public static C2SRequestPlayerRevive decode(final PacketBuffer packet_buffer) {
        return new C2SRequestPlayerRevive(packet_buffer.readUUID(), packet_buffer.readEnum(Hand.class));
    }

    public static void handle(final C2SRequestPlayerRevive msg, final Supplier<NetworkEvent.Context> context_supplier) {
        final NetworkEvent.Context context = context_supplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = TotemOfReviving.players.getPlayer(msg.player_uuid);
            if (sender == null) { return; }

            ItemStack item = sender.getItemInHand(msg.hand);
            if (item.getOrCreateTag().getInt(TotemOfRevivingItem.TAG_TARGET_INDEX) > TotemOfReviving.players.getPlayerCount()-1) {
                sender.sendMessage(new StringTextComponent(TextFormatting.RED + "Error getting target! (Try selecting the target again)"), sender.getUUID());
            } else {
                ServerPlayerEntity player_to_revive = TotemOfReviving.players.getPlayerByName(item.getOrCreateTag().getString(TotemOfRevivingItem.TAG_TARGET_NAME));
                ServerWorld player_to_revive_world = player_to_revive.getLevel();
                ServerWorld sender_world = sender.getLevel();
                int required_charge = player_to_revive.getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS));
                if (player_to_revive.isSpectator()) {
                    if (player_to_revive_world.equals(sender_world)) {
                        if (item.getOrCreateTag().getInt(TotemOfRevivingItem.TAG_CHARGE_AMOUNT) >= required_charge) {
                            player_to_revive.teleportTo(sender.getX(), sender.getY(), sender.getZ());
                            player_to_revive.setGameMode(GameType.SURVIVAL);
                            item.getOrCreateTag().putInt(TotemOfRevivingItem.TAG_CHARGE_AMOUNT, item.getOrCreateTag().getInt(TotemOfRevivingItem.TAG_CHARGE_AMOUNT) - required_charge);
                            sender.sendMessage(new StringTextComponent(TextFormatting.AQUA + "Successfully revived " + TextFormatting.BLUE + player_to_revive.getDisplayName().getString()), sender.getUUID());
                        } else {
                            sender.sendMessage(new StringTextComponent(TextFormatting.AQUA + "Not enough charge! Required charge is: " + TextFormatting.BLUE + required_charge), sender.getUUID());
                        }
                    } else {
                        sender.sendMessage(new StringTextComponent(TextFormatting.BLUE + player_to_revive.getDisplayName().getString() + TextFormatting.AQUA + " is not in this dimension!"), sender.getUUID());
                    }
                } else {
                    sender.sendMessage(new StringTextComponent(TextFormatting.BLUE + player_to_revive.getDisplayName().getString() + TextFormatting.AQUA + " is not dead!"), sender.getUUID());
                }
            }
        });
        context.setPacketHandled(true);
    }
}
