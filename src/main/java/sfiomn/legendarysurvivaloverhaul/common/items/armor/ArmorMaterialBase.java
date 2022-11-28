package sfiomn.legendarysurvivaloverhaul.common.items.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.function.Supplier;

public class ArmorMaterialBase implements IArmorMaterial
{
	private static final int[] MAX_DAMAGE_ARRAY = new int[] {13, 15, 16, 11};
	private final String name;
	private final float maxDamageFactor;
	private final int[] damageReductionAmountArray;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyValue<Ingredient> repairMaterial;
	
	public ArmorMaterialBase(String name, float maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial)
	{
		this.name = name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairMaterial = new LazyValue<Ingredient>(repairMaterial);
	}
	
	@Override
	public int getDurabilityForSlot(EquipmentSlotType slotIn)
	{
		return (int) (MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor);
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType slotIn)
	{
		return this.damageReductionAmountArray[slotIn.getIndex()];
	}

	@Override
	public int getEnchantmentValue()
	{
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound()
	{
		return this.soundEvent;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return this.repairMaterial.get();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getName()
	{
		return LegendarySurvivalOverhaul.MOD_ID + ":" + this.name;
	}

	@Override
	public float getToughness()
	{
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance()
	{
		return this.knockbackResistance;
	}
}
