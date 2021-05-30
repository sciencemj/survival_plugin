package sciencemj.survival_plugin;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;

public final class Survival_plugin extends JavaPlugin {
    public static Plugin plugin;
    public static FileConfiguration config;
    @Override
    public void onEnable() {
        // Plugin startup logic
        config = getConfig();
        plugin = this;
        CustomEnchants.register();
        Bukkit.getServer().getPluginManager().registerEvents(new EventMain(), this);
        Bukkit.getServer().getPluginCommand("story_start").setExecutor(new CommandMain());
        Bukkit.getServer().getPluginCommand("enc").setExecutor(new CommandMain());
        //------------------------------------------setting-----------------------------------------------
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new Runnable(){
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
            }
        },20L, 4800L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new Runnable(){
            @Override
            public void run() {
                EventMain.digCount.forEach((key, value)
                    -> {
                    if (value > 0) {
                        EventMain.digCount.put(key, value - 1);
                    }
                });
                EventMain.atkCount.forEach((key, value)
                        -> {
                    if (value > 0) {
                        EventMain.atkCount.put(key, value - 1);
                    }
                });
            }
        },0L, 70L);
        //-------------------------------------scheduler---------------------------------------------
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        config.set("gameClear", EventMain.gameClear);
        saveConfig();
    }
}
