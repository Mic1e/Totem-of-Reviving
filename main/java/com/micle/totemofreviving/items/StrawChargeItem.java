package com.micle.totemofreviving.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;

public class StrawChargeItem extends Item {
    public StrawChargeItem() {
        super(new Properties().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON));
    }
}
