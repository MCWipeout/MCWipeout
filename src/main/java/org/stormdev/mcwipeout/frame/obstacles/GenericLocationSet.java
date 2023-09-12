package org.stormdev.mcwipeout.frame.obstacles;
/*
  Created by Stormbits at 9/12/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.stormdev.mcwipeout.utils.WLocation;

import java.util.Map;

public class GenericLocationSet {

    @Getter private Map<WLocation, Material> map;

    public GenericLocationSet(Map<WLocation, Material> map) {
        this.map = map;
    }
}
