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

public class TotemOfRevivingItem extends Item {
    public static final String TAG_CHARGE_AMOUNT = "charge";
    public static final String TAG_TARGET_INDEX = "target_index";
    public static final String TAG_TARGET_NAME = "target_name";

    public TotemOfRevivingItem() {
        super(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).rarity(Rarity.RARE));
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new StringTextComponent(TextFormatting.DARK_AQUA + "Charges: " + TextFormatting.BLUE + stack.getOrCreateTag().getInt(TAG_CHARGE_AMOUNT)));
        tooltip.add(new StringTextComponent(TextFormatting.DARK_AQUA + "Target: " + TextFormatting.BLUE + stack.getOrCreateTag().getString(TAG_TARGET_NAME)));
        tooltip.add(new StringTextComponent(""));
        if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new StringTextComponent(TextFormatting.AQUA + "R-CLICK"));
            tooltip.add(new StringTextComponent(TextFormatting.DARK_AQUA + "When other hand is empty: attempt to revive target."));
            tooltip.add(new StringTextComponent(TextFormatting.DARK_AQUA + "When other hand has " + TextFormatting.BLUE + "Reviving Charge" + TextFormatting.DARK_AQUA + ": charge totem."));
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent(TextFormatting.AQUA + "SHIFT R-CLICK"));
            tooltip.add(new StringTextComponent(TextFormatting.DARK_AQUA + "Cycle through available targets."));
        } else {
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + "[" + TextFormatting.WHITE + "LSHIFT" + TextFormatting.GRAY + "] for advanced tooltip."));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (player.isCrouching()) {
            TotemOfReviving.INSTANCE.sendToServer(new C2SRequestTotemTarget(player.getUniqueID(), hand));
        } else {
            Hand item_charge_hand = Hand.MAIN_HAND;
            if (hand.equals(Hand.MAIN_HAND)) {
                item_charge_hand = Hand.OFF_HAND;
            }
            Item item_charge = player.getHeldItem(item_charge_hand).getItem();

            if (item_charge instanceof RevivingChargeItem) {
                TotemOfReviving.INSTANCE.sendToServer(new C2SRequestTotemCharge(player.getUniqueID(), hand, item_charge_hand));
            } else {
                TotemOfReviving.INSTANCE.sendToServer(new C2SRequestPlayerRevive(player.getUniqueID(), hand));
            }
        }
        return super.onItemRightClick(world, player, hand);
    }
}
