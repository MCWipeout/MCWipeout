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
        playersOnItem = new ItemStack(Material.GHAST_TEAR);
        ItemMeta meta = playersOnItem.getItemMeta();
        meta.setDisplayName("§aPlayers enabled");
        meta.setCustomModelData(10022);
        playersOnItem.setItemMeta(meta);

        playersOffItem = new ItemStack(Material.GHAST_TEAR);
        ItemMeta metaOff = playersOffItem.getItemMeta();
        metaOff.setDisplayName("§cPlayers disabled");
        metaOff.setCustomModelData(10023);
        playersOffItem.setItemMeta(metaOff);

        rewindItem = new ItemStack(Material.GHAST_TEAR);
        ItemMeta rewindMeta = rewindItem.getItemMeta();
        rewindMeta.setDisplayName("§6Rewind");
        rewindMeta.setCustomModelData(10021);
        rewindItem.setItemMeta(rewindMeta);
    }
}
