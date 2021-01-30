package icey.survivaloverhaul.common.enchantments;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.registry.EffectRegistry;
import icey.survivaloverhaul.registry.EnchantRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class InsulationMagic extends GenericMagic
{	
	private static EquipmentSlotType[] slots = new EquipmentSlotType[] {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
	private MagicType MT;
	/**
	 * @param name of enchant
	 */
	public InsulationMagic(String name, MagicType MT) 
	{
		super(Rarity.RARE, EnchantmentType.WEARABLE, slots, new EnchantOptions(5));
		this.setRegistryName(Main.MOD_ID, name);
		this.MT = MT;
	}
	private static int skip = 0;
	@SubscribeEvent
	public static void Effect(LivingUpdateEvent event) 
	{
		LivingEntity entity = event.getEntityLiving();
		if (entity.getEntityWorld().isRemote)
			return;
		/*
		int ab = EnchantmentHelper.getMaxEnchantmentLevel(EnchantRegistry.ModEnchants.ADAPTIVE_BARRIER, entity),
			h = EnchantmentHelper.getMaxEnchantmentLevel(EnchantRegistry.ModEnchants.THERMAL_BARRIER, entity),
			c = EnchantmentHelper.getMaxEnchantmentLevel(EnchantRegistry.ModEnchants.COLD_BARRIER, entity);
		if (skip == 0) 
		{
			//apply effects or modifiers
			if (ab > 0) 
			{
				entity.addPotionEffect(new EffectInstance(EffectRegistry.ModEffects.COLD_RESISTANCE, 500, 0, true, false));
				entity.addPotionEffect(new EffectInstance(EffectRegistry.ModEffects.HEAT_RESISTANCE, 500, 0, true, false));
			}
			else if (h > 0)
				entity.addPotionEffect(new EffectInstance(EffectRegistry.ModEffects.HEAT_RESISTANCE, 500, 0, true, false));
			else if (c > 0)
				entity.addPotionEffect(new EffectInstance(EffectRegistry.ModEffects.COLD_RESISTANCE, 500, 0, true, false));
			skip++;// re-apply effects after a few seconds
		}
		else if(skip == 100)
			skip = 0;
		else
			skip++;
		*/
	}
	//not used atm
	public enum MagicType
	{
		Both(1),
		Heat(2),
		Cool(3);
		private int Type;
		
		public int getType() {
			return Type;
		}

		MagicType(int i) 
		{
			Type = i;
		}
	}
}
