package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

@OnlyIn(Dist.CLIENT)
public class KeybindingRegistry {
    public static KeyBinding showAddedDesc;

    public static void register() {
        //  LEFT SHIFT default key binding : 340
        showAddedDesc = create("added_desc", 340);
        ClientRegistry.registerKeyBinding(showAddedDesc);
    }

    private static KeyBinding create(String name, int key) {
        return new KeyBinding("key." + LegendarySurvivalOverhaul.MOD_ID + "." + name, KeyConflictContext.GUI, InputMappings.Type.KEYSYM, key, "key."+ LegendarySurvivalOverhaul.MOD_ID + "." + name + ".title");
    }
}
