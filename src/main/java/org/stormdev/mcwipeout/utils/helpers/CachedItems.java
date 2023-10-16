package org.stormdev.mcwipeout.utils.helpers;
/*
  Created by Stormbits at 10/9/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class CachedItems {

    public static ItemStack playersOnItem;
    public static ItemStack playersOffItem;
    public static ItemStack rewindItem;

    public CachedItems() {
        playersOffItem = new ItemStack(Material.GHAST_TEAR);
        ItemMeta meta = playersOffItem.getItemMeta();
        meta.setDisplayName("§aEnable Players");
        meta.setCustomModelData(10022);
        playersOffItem.setItemMeta(meta);

        playersOnItem = new ItemStack(Material.GHAST_TEAR);
        ItemMeta metaOff = playersOnItem.getItemMeta();
        metaOff.setDisplayName("§cDisable Players");
        metaOff.setCustomModelData(10023);
        playersOnItem.setItemMeta(metaOff);

        rewindItem = new ItemStack(Material.GHAST_TEAR);
        ItemMeta rewindMeta = rewindItem.getItemMeta();
        rewindMeta.setDisplayName("§6Respawn");
        rewindMeta.setCustomModelData(10021);
        rewindItem.setItemMeta(rewindMeta);
    }
}
