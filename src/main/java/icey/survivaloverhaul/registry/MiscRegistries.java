package icey.survivaloverhaul.registry;

import icey.survivaloverhaul.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class MiscRegistries
{
	public static final class SoundEvents 
	{
		public static final SoundEvent ENTITY_FREEZE_ENTER = new SoundEvent(new ResourceLocation(Main.MOD_ID, "entity.generic.freeze.enter"));
		public static final SoundEvent ENTITY_FREEZE_EXIT = new SoundEvent(new ResourceLocation(Main.MOD_ID, "entity.generic.freeze.exit"));
		
		public static final SoundEvent PLAYER_SIZZLE_LOOP = new SoundEvent(new ResourceLocation(Main.MOD_ID, "player.overheat.loop"));
	}
	
	public static final class EntityTypeTags
	{
		public static final ResourceLocation FREEZING_IMMUNE = new ResourceLocation(Main.MOD_ID, "freezing_immune");
		public static final ResourceLocation FREEZING_WEAK = new ResourceLocation(Main.MOD_ID, "freezing_weak");
	}
	
	public static final class BlockTypeTags
	{
		public static final ResourceLocation FREE_CLIMBABLE = new ResourceLocation(Main.MOD_ID, "free_climbable");
	}
}
