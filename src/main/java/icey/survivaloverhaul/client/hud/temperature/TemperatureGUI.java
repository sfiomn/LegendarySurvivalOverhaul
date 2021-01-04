package icey.survivaloverhaul.client.hud.temperature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import icey.survivaloverhaul.Main;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class TemperatureGUI
{
	private final Minecraft mc = Minecraft.getInstance();
	private final MainWindow window = mc.getMainWindow();
	
	private int updateCounter = 0;
	private final Random rand = new Random();
	
	public static final ResourceLocation ICONS = new ResourceLocation(Main.MOD_ID + ":textures/gui/overlay.png");
	
	private static final int texturepos_X = 0;
	private static final int texturepos_Y = 57;
	
	private static final int textureWidth = 16;
	private static final int textureHeight = 16;
	
	private void bind(ResourceLocation resource)
	{
		mc.getTextureManager().bindTexture(resource);
	}
}
