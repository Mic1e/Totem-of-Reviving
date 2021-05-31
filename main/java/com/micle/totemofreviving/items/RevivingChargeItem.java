package com.micle.totemofreviving.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;

public class RevivingChargeItem extends Item {
    public RevivingChargeItem() {
        super(new Item.Properties().group(ItemGroup.MISC).rarity(Rarity.RARE));
    }
}
