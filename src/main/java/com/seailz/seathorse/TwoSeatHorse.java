package com.seailz.seathorse;

import com.seailz.seathorse.listeners.HorseMoveListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TwoSeatHorse extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new HorseMoveListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
