package sciencemj.survival_plugin;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EventMain implements Listener {
    HashMap<Player, PlayerStatus> statuses = new HashMap<Player, PlayerStatus>();
    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            Random r = new Random();
            int i = r.nextInt(100);
            if (e.getDamage() >= 5){  // 중상
                if (i < 40){ //골절
                    action(p, "bone");
                }else if (i < 60){
                    action(p, "bleed");
                }else if (i < 80){
                    action(p, "critical");
                }
            }else if (e.getDamage() >= 2){
                if (i < 20){ //골절
                    action(p, "bone");
                }else if (i < 30){
                    action(p, "bleed");
                }else if (i < 40){
                    action(p, "critical");
                }
            }
        }
    }
    public void action(Player p, String s){
        switch (s){
            case "bone":
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.RED+ "골절 당했습니다!"));
                if (statuses.containsKey(p)){
                    statuses.get(p).getPlayerEffects().put(Status.BreakBones, 5);
                }else{
                    PlayerStatus ps = new PlayerStatus(p);
                    ps.getPlayerEffects().put(Status.BreakBones, 5);
                    ps.run();
                    statuses.put(p, ps);
                }
                break;
            case "bleed":
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.RED+ "출혈 중 입니다!"));
                if (statuses.containsKey(p)){
                    statuses.get(p).getPlayerEffects().put(Status.Bleed, 5);
                }else{
                    PlayerStatus ps = new PlayerStatus(p);
                    ps.getPlayerEffects().put(Status.Bleed, 5);
                    ps.run();
                    statuses.put(p, ps);
                }
                break;
            case "critical":
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.RED+ "급소 공격 당했습니다!"));
                if (statuses.containsKey(p)){
                    statuses.get(p).getPlayerEffects().put(Status.Critical, 1);
                }else{
                    PlayerStatus ps = new PlayerStatus(p);
                    ps.getPlayerEffects().put(Status.Critical, 1);
                    ps.run();
                    statuses.put(p, ps);
                }
                break;
            default:
                break;
        }
    }
}
