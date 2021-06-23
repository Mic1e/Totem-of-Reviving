package com.micle.totemofreviving.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;

public class StrawChargeItem extends Item {
    public StrawChargeItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC).rarity(Rarity.UNCOMMON));
    }
}
