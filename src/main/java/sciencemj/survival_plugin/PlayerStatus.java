package sciencemj.survival_plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
@RequiredArgsConstructor
public class PlayerStatus {
    private final HashMap<Status, Integer> playerEffects = new HashMap<Status, Integer>();
    private final Player player;
    public void run(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Survival_plugin.plugin, new Runnable() {
            @Override
            public void run() {
                for (Status key:playerEffects.keySet()) {
                    player.addPotionEffect(key.getEffect());
                    if (playerEffects.get(key) - 1 > 0) {
                        playerEffects.put(key, playerEffects.get(key) - 1);
                    }else{
                        playerEffects.remove(key);
                    }
                }
            }
        },0L, 20L);
    }
}
