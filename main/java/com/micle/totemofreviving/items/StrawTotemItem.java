package com.micle.totemofreviving.items;

import com.micle.totemofreviving.TotemOfReviving;
import com.micle.totemofreviving.network.C2SRequestPlayerRevive;
import com.micle.totemofreviving.network.C2SRequestTotemCharge;
import com.micle.totemofreviving.network.C2SRequestTotemTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class StrawTotemItem extends Item {
    public static final String TAG_CHARGE_AMOUNT = "charge";
    public static final String TAG_TARGET_INDEX = "target_index";
    public static final String TAG_TARGET_NAME = "target_name";
    public static final String TAG_FAIL_CHANCE = "fail_chance";
    public static final int STARTING_FAIL_CHANCE = 45;

    public StrawTotemItem() {
        super(new Properties().group(ItemGroup.MISC).maxStackSize(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new StringTextComponent(TextFormatting.GOLD + "Charges: " + TextFormatting.GRAY + stack.getOrCreateTag().getInt(TAG_CHARGE_AMOUNT)));
        tooltip.add(new StringTextComponent(TextFormatting.GOLD + "Target: " + TextFormatting.GRAY + stack.getOrCreateTag().getString(TAG_TARGET_NAME)));
        tooltip.add(new StringTextComponent(TextFormatting.GOLD + "Fail Chance: " + TextFormatting.GRAY + stack.getOrCreateTag().getInt(TAG_FAIL_CHANCE)));
        tooltip.add(new StringTextComponent( TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + "\"Feels kinda funky.\""));
        tooltip.add(new StringTextComponent(""));
        if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "R-CLICK"));
            tooltip.add(new StringTextComponent(TextFormatting.GOLD + "When other hand is empty: attempt to revive target."));
            tooltip.add(new StringTextComponent(TextFormatting.GOLD + "When other hand has " + TextFormatting.GRAY + "Straw Reviving Charge" + TextFormatting.GOLD + ": charge totem."));
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "SHIFT R-CLICK"));
            tooltip.add(new StringTextComponent(TextFormatting.GOLD + "Cycle through available targets."));
        } else {
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + "[" + TextFormatting.WHITE + "LSHIFT" + TextFormatting.GRAY + "] for advanced tooltip."));
        }
    }

    @Override
    public void onCreated(ItemStack stack, World world, PlayerEntity player) {
        super.onCreated(stack, world, player);
        stack.getOrCreateTag().putInt(TAG_CHARGE_AMOUNT, 0);
        stack.getOrCreateTag().putInt(TAG_FAIL_CHANCE, STARTING_FAIL_CHANCE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (world.isRemote) { return super.onItemRightClick(world, player, hand); }
        if (player.isCrouching()) {
            TotemOfReviving.INSTANCE.sendToServer(new C2SRequestTotemTarget(player.getUniqueID(), hand));
        } else {
            Hand item_charge_hand = Hand.MAIN_HAND;
            if (hand.equals(Hand.MAIN_HAND)) {
                item_charge_hand = Hand.OFF_HAND;
            }
            Item item_charge = player.getHeldItem(item_charge_hand).getItem();

            if (item_charge instanceof StrawChargeItem) {
                TotemOfReviving.INSTANCE.sendToServer(new C2SRequestTotemCharge(player.getUniqueID(), hand, item_charge_hand));
            } else {
                TotemOfReviving.INSTANCE.sendToServer(new C2SRequestPlayerRevive(player.getUniqueID(), hand));
            }
        }
        return super.onItemRightClick(world, player, hand);
    }
}
