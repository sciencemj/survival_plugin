package sciencemj.survival_plugin;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.block.EnchantingTable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EventMain implements Listener {
    HashMap<Player, PlayerStatus> statuses = new HashMap<Player, PlayerStatus>();
    public static HashMap<Player, Integer> digCount = new HashMap<Player, Integer>();
    public static HashMap<Player, Integer> atkCount = new HashMap<Player, Integer>();
    String[] number = new String[] {"I", "II", "III"};
    public static boolean gameClear = Survival_plugin.config.getBoolean("gameClear");

    @EventHandler
    public void onEnchant(EnchantItemEvent e){
        if(e.getEnchantBlock().getType().equals(Material.ENCHANTING_TABLE)){
            int cost = e.getExpLevelCost();
            ItemStack item = e.getItem();
            ItemMeta meta = item.getItemMeta();
            Random r = new Random();
            if(r.nextInt(50) < cost && cost >= 10 && EnchantmentTarget.WEAPON.includes(item.getType())){
                assert meta != null;
                meta.addEnchant(CustomEnchants.attackEnchant, (int)cost/10 - 1, false);
                List<String> lore = meta.getLore();
                if(lore == null){
                    lore = new ArrayList<String>();
                }
                lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "공격 피로 감소 " + number[(int)cost/10 - 1]);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }else if(r.nextInt(50) < cost && cost >= 10 && EnchantmentTarget.TOOL.includes(item.getType())){
                assert meta != null;
                meta.addEnchant(CustomEnchants.digEnchant, (int)cost/10 - 1, false);
                List<String> lore = meta.getLore();
                if(lore == null){
                    lore = new ArrayList<String>();
                }
                lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "채굴 피로 감소 " + number[(int)cost/10 - 1]);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }
    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent e){
        if (e.getEntity() instanceof Player && !gameClear){
            Player p = (Player) e.getEntity();
            Random r = new Random();
            int i = r.nextInt(100);
            if (e.getDamage() >= 7){ // 치명상
                action(p, "bone");
                if (i < 75){
                    action(p, "bleed");
                }else if (i < 99){
                    action(p, "critical");
                }
            }else if (e.getDamage() >= 5){  // 중상
                if (i < 40){ //골절
                    action(p, "bone");
                }else if (i < 60){
                    action(p, "bleed");
                }else if (i < 75){
                    action(p, "critical");
                }
            }else if (e.getDamage() >= 2){
                if (i < 20){ //골절
                    action(p, "bone");
                }else if (i < 30){
                    action(p, "bleed");
                }else if (i < 37){
                    action(p, "critical");
                }
            }
        }
    }
    @EventHandler
    public void playerDigEvent(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (!gameClear) {
            ItemStack item = p.getInventory().getItemInMainHand();
            Random r = new Random();
            Map<Enchantment, Integer> enchants = item.getEnchantments();
            if (item != null && enchants != null) {
                if (enchants.containsKey(CustomEnchants.digEnchant)){
                    if (r.nextInt(40) <= (10 * enchants.get(CustomEnchants.digEnchant))) {
                        return;
                    }
                }
            }
            if (digCount.containsKey(p)) {
                digCount.put(p, digCount.get(p) + 1);
                if (digCount.get(p) >= 10) {
                    action(p, "dig");
                    digCount.remove(p);
                }
            } else {
                digCount.put(p, 1);
            }
        }
    }

    @EventHandler
    public void playerAtkEvent(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player && !gameClear){
            Player p = (Player) e.getDamager();
            ItemStack item = p.getInventory().getItemInMainHand();
            Random r = new Random();
            Map<Enchantment, Integer> enchants = item.getEnchantments();
            if (item != null && enchants != null) {
                if (enchants.containsKey(CustomEnchants.attackEnchant)){
                    if (r.nextInt(40) <= (10 * enchants.get(CustomEnchants.attackEnchant))) {
                        actionBar(p,ChatColor.YELLOW + "공격할 힘이 납니다!");
                        return;
                    }
                }
            }
            if (atkCount.containsKey(p)){
                atkCount.put(p, atkCount.get(p) + 1);
                if (atkCount.get(p) >= 3){
                    action(p, "attack");
                    atkCount.remove(p);
                }
            }else {
                atkCount.put(p, 1);
            }
            if (e.getEntity().getType().equals(EntityType.ENDER_DRAGON)){
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20,0,true,false));
            }
        }
    }

    @EventHandler
    public void killEnderDragon(PlayerAdvancementDoneEvent e){
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (e.getAdvancement().getKey().getKey().equals("end/kill_dragon")) {
                //p.sendMessage(e.getEventName() + " // " + e.getAdvancement().getKey().getKey());
                p.playSound(p.getLocation(), Sound.MUSIC_DISC_PIGSTEP, 10, 1);
                p.sendTitle(ChatColor.GREEN + "엔더 드래곤을 격파했습니다!", "", 20, 40, 20);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Survival_plugin.plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.sendTitle(ChatColor.GREEN + "세계에 평화가 찾아옵니다", "난이도 쉬움,HARDMODE:OFF", 20, 40, 20);
                        p.getWorld().setDifficulty(Difficulty.EASY);
                        gameClear = true;
                        Survival_plugin.config.set("gameClear", true);
                        for (int i = 1; i < 6; i++) {
                            Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.setPower(i);
                            meta.addEffect(FireworkEffect.builder()
                                    .trail(true)
                                    .withColor(Color.YELLOW, Color.GREEN)
                                    .flicker(true)
                                    .with(FireworkEffect.Type.STAR).build());
                            firework.setFireworkMeta(meta);
                        }
                    }
                }, 80);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.sendMessage(ChatColor.GREEN + "플레이어: " + p.getDisplayName());
        p.sendMessage(ChatColor.RED + "HARDMODE VERSION: " + Survival_plugin.plugin.getDescription().getVersion());
        p.sendMessage(ChatColor.GREEN + "[V.1.1 patched] 채굴/공격 피로 감소 인챈트 추가");
        p.sendMessage(ChatColor.YELLOW + "GAME CLEAR: " + gameClear);
    }
    public void action(Player p, String s){
        if (statuses.containsKey(p)){
            switch (s) {
                case "bone":
                    actionBar(p,ChatColor.RED + "골절 당했습니다!");
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.BreakBones, 5);
                    }
                    break;
                case "bleed":
                    actionBar(p,ChatColor.RED + "출혈 중 입니다!");
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.Bleed, 5);
                    }
                    break;
                case "critical":
                    actionBar(p,ChatColor.RED + "급소 공격 당했습니다!");
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.Critical, 1);
                    }
                    break;
                case "dig":
                    actionBar(p,ChatColor.RED + "블럭을 캐다 지쳤습니다!");
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.StressDig, 7);
                    }
                    break;
                case "attack":
                    actionBar(p,ChatColor.RED + "공격하다 지쳤습니다!");
                    if (statuses.containsKey(p)) {
                        statuses.get(p).getPlayerEffects().put(Status.StressAtk, 5);
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
    public void actionBar(Player p , String message){
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
