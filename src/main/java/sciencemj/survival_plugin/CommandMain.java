package sciencemj.survival_plugin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandMain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("story_start")){
            for (Player p: Bukkit.getOnlinePlayers()) {
                p.sendTitle("어느날 엔더 드래곤이 나타나고", "세상이 혼란에 빠졌습니다",20,40,10);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10,1);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Survival_plugin.plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 10,1);
                        p.sendTitle("엔더 드래곤을 물리치고", "평화를 칮으세요!", 20,40,10);
                    }
                },70L);
            }
        }else if(cmd.getName().equals("enc")){
            if(sender instanceof Player){
                Player p = ((Player) sender).getPlayer();
                ItemStack item = p.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                if (!meta.hasEnchant(CustomEnchants.attackEnchant)) {
                    meta.addEnchant(CustomEnchants.attackEnchant, 1, true);
                }else {
                    p.sendMessage("already enchanted");
                }
                item.setItemMeta(meta);
            }
        }
        return true;
    }
}
