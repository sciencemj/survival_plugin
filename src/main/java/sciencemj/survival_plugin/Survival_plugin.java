package sciencemj.survival_plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Survival_plugin extends JavaPlugin {
    public static Plugin plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new Runnable(){
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
            }
        },20L, 2400L);
        Bukkit.getServer().getPluginManager().registerEvents(new EventMain(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
