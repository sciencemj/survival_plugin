package sciencemj.survival_plugin;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EventMain implements Listener {
    HashMap<Player, PlayerStatus> statuses = new HashMap<Player, PlayerStatus>();
    public static HashMap<Player, Integer> digCount = new HashMap<Player, Integer>();
    public static HashMap<Player, Integer> atkCount = new HashMap<Player, Integer>();
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
    @EventHandler
    public void playerDigEvent(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (digCount.containsKey(p)){
            digCount.put(p, digCount.get(p) + 1);
            if (digCount.get(p) >= 10){
                action(p, "dig");
                digCount.remove(p);
            }
        }else {
            digCount.put(p, 1);
        }
    }

    @EventHandler
    public void playerAtkEvent(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            if (atkCount.containsKey(p)){
                atkCount.put(p, atkCount.get(p) + 1);
                if (atkCount.get(p) >= 5){
                    action(p, "attack");
                    atkCount.remove(p);
                }
            }else {
                atkCount.put(p, 1);
            }
        }
    }
    public void action(Player p, String s){
        if (statuses.containsKey(p)){
            switch (s) {
                case "bone":
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.RED + "골절 당했습니다!"));
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.BreakBones, 5);
                    }
                    break;
                case "bleed":
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.RED + "출혈 중 입니다!"));
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.Bleed, 5);
                    }
                    break;
                case "critical":
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.RED + "급소 공격 당했습니다!"));
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.Critical, 1);
                    }
                    break;
                case "dig":
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.RED + "블럭을 캐다 지쳤습니다!"));
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.StressDig, 5);
                    }
                    break;
                case "attack":
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.RED + "공격하다 지쳤습니다!"));
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.StressAtk, 3);
                    }
                    break;
                default:
                    break;
            }
        }else {
            PlayerStatus ps = new PlayerStatus(p);
            ps.run();
            statuses.put(p, ps);
            action(p, s);
        }
    }
}
