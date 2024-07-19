package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;


@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyMappingRegistry {
    public static KeyMapping showAddedDesc;
    public static KeyMapping showBodyHealth;

    @SubscribeEvent
    public static void registerKeyMappingsEvent(RegisterKeyMappingsEvent event) {
        showAddedDesc = create("added_desc", GLFW.GLFW_KEY_LEFT_SHIFT, KeyConflictContext.GUI);
        event.register(showAddedDesc);
        showBodyHealth = create("body_health", GLFW.GLFW_KEY_H, KeyConflictContext.UNIVERSAL);
        event.register(showBodyHealth);
    }

    private static KeyMapping create(String name, int key, KeyConflictContext context) {
        return new KeyMapping("key." + LegendarySurvivalOverhaul.MOD_ID + "." + name, context, InputConstants.Type.KEYSYM, key, "key."+ LegendarySurvivalOverhaul.MOD_ID + ".title");
    }
}
