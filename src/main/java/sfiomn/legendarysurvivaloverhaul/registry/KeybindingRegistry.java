package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

@OnlyIn(Dist.CLIENT)
public class KeybindingRegistry {
    public static KeyBinding showAddedDesc;
    public static KeyBinding showBodyHealth;

    public static void register() {
        //  LEFT SHIFT default key binding : 340
        showAddedDesc = create("added_desc", GLFW.GLFW_KEY_LEFT_SHIFT, KeyConflictContext.GUI);
        ClientRegistry.registerKeyBinding(showAddedDesc);
        if (Config.Baked.localizedBodyDamageEnabled) {
            showBodyHealth = create("body_health", GLFW.GLFW_KEY_H, KeyConflictContext.UNIVERSAL);
            ClientRegistry.registerKeyBinding(showBodyHealth);
        }
    }

    private static KeyBinding create(String name, int key, KeyConflictContext context) {
        return new KeyBinding("key." + LegendarySurvivalOverhaul.MOD_ID + "." + name, context, InputMappings.Type.KEYSYM, key, "key."+ LegendarySurvivalOverhaul.MOD_ID + ".title");
    }
}
