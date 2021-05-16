package sciencemj.survival_plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@RequiredArgsConstructor
public enum Status {
    BreakBones("break_bones", new PotionEffect(PotionEffectType.SLOW, 20, 1));

    private final String name;
    private final PotionEffect effect;
}
