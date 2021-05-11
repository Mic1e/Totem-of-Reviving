package com.micle.totemofreviving.setup;

import com.micle.totemofreviving.items.RevivingChargeItem;
import com.micle.totemofreviving.items.TotemOfRevivingItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    public static final RegistryObject<Item> TOTEM_OF_REVIVING = Registration.ITEMS.register("totem_of_reviving", TotemOfRevivingItem::new);
    public static final RegistryObject<Item> REVIVING_CHARGE = Registration.ITEMS.register("reviving_charge", RevivingChargeItem::new);

    static void register() {
    }
}
