package icey.survivaloverhaul.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class GenericMagic extends Enchantment 
{
	public static class EnchantOptions
	{
		private int min = 1, max = 1, minability = 1, minmultiplier = 10, maxability = 5;
		private boolean book = true;
		
		public EnchantOptions(int max) 
		{
			this(1, max, 1, 10, 5, true);
		}
		
		public EnchantOptions(int min, int max, int maxability, boolean book) 
		{
			this(min, max, 1, 10, maxability, book);
		}
		
		/**
		 * Gives a neat way of setting options.
		 * @param minimal level
		 * @param maximum level
		 * @param minimal enchant ability
		 * @param minimal enchant ability multiplier
		 * @param max enchant ability
		 * @param can enchant books
		 */
		
		public EnchantOptions(int min, int max, int minability, int minmultiplier, int maxability, boolean book) 
		{
			this.min = min;
			this.max = max;
			this.minability = minability;
			this.minmultiplier = minmultiplier;
			this.maxability = maxability;
			this.book = book;
		}
	}
	EnchantOptions EO;
	protected GenericMagic(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots, EnchantOptions EO) 
	{
		super(rarityIn, typeIn, slots);
		this.EO = EO;
	}
	
	/**
	* Returns the minimum level that the enchantment can have.
	*/
	@Override
	public int getMinLevel() 
	{
	   return EO.min;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel() 
	{
	   return EO.max;
	}
	
	/**
	* Returns the minimal value of enchantability needed on the enchantment level passed.
	*/
	@Override
	public int getMinEnchantability(int enchantmentLevel) 
	{
		return EO.minability + enchantmentLevel * EO.minmultiplier;
	}

	public int getMaxEnchantability(int enchantmentLevel)
	{
		return this.getMinEnchantability(enchantmentLevel) + EO.maxability;
	}
	
	@Override
	public boolean isAllowedOnBooks() 
	{
	   return EO.book;
	}
}
