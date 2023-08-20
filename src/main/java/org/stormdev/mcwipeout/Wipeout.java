package org.stormdev.mcwipeout;

import org.bukkit.plugin.java.JavaPlugin;

public final class Wipeout extends JavaPlugin {

    private static Wipeout plugin;

    public static Wipeout get() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

    }

    @Override
    public void onDisable() {
        plugin = null;
    }
}
