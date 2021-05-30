package sciencemj.survival_plugin;

import org.bukkit.enchantments.Enchantment;
import sciencemj.survival_plugin.enchants.AttackEnchant;
import sciencemj.survival_plugin.enchants.DigEnchant;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomEnchants {
    public static final Enchantment attackEnchant = new AttackEnchant();
    public static final Enchantment digEnchant = new DigEnchant();
    public static final Enchantment[] all = new Enchantment[] {attackEnchant, digEnchant};

    public static void register(){
        for (Enchantment enchant : all) {
            boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchant);
            if (!registered) {
                registerEnchantment(enchant);
            } else {
                System.out.println("[HardCore] Enchantment " + enchant.getKey() + " already loaded!");
            }
        }
    }
    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if(registered){
            System.out.println("[HardCore] Enchantment " + enchantment.getKey() + " registered!");
        }
    }

}
