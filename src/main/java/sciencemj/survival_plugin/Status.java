package sciencemj.survival_plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@RequiredArgsConstructor
public enum Status {
    BreakBones("break_bones", new PotionEffect(PotionEffectType.SLOW, 20, 2, true, false)),
    Bleed("bleed", new PotionEffect(PotionEffectType.WITHER, 20, 1, true, false)),
    Critical("critical", new PotionEffect(PotionEffectType.HARM, 20, 0, true,false));

    private final String name;
    private final PotionEffect effect;
}
