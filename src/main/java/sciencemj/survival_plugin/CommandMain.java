package sciencemj.survival_plugin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        }
        return true;
    }
}
