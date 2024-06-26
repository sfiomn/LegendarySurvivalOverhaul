package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;


public class KeyMappingRegistry {
    public static KeyMapping showAddedDesc;
    public static KeyMapping showBodyHealth;

    public static void registerKeyMappingEvent(RegisterKeyMappingsEvent event) {
        //  LEFT SHIFT default key binding : 340
        showAddedDesc = create("added_desc", GLFW.GLFW_KEY_LEFT_SHIFT, KeyConflictContext.GUI);
        event.register(showAddedDesc);
        if (Config.Baked.localizedBodyDamageEnabled) {
            showBodyHealth = create("body_health", GLFW.GLFW_KEY_H, KeyConflictContext.UNIVERSAL);
            event.register(showBodyHealth);
        }
    }

    private static KeyMapping create(String name, int key, KeyConflictContext context) {
        return new KeyMapping("key." + LegendarySurvivalOverhaul.MOD_ID + "." + name, context, InputConstants.Type.KEYSYM, key, "key."+ LegendarySurvivalOverhaul.MOD_ID + ".title");
    }
}
