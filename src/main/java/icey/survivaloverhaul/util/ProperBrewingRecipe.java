package icey.survivaloverhaul.util;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;

@SuppressWarnings("unused")
public class ProperBrewingRecipe extends BrewingRecipe
{
	private final Ingredient input;
	private final Ingredient ingredient;
	private final ItemStack output;
	
	public ProperBrewingRecipe(Ingredient input, Ingredient ingredient, ItemStack output)
	{
		super(input, ingredient, output);
		this.input = input;
		this.ingredient = ingredient;
		this.output = output;
	}
	
	@Override
	public boolean isInput(@Nonnull ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}
		else
		{
			ItemStack[] matchingStacks = input.getMatchingStacks();
			if (matchingStacks.length == 0)
			{
				return stack.isEmpty();
			}
			else
			{
				for (ItemStack itemStack : matchingStacks)
				{
					if (itemStack.isItemEqual(stack) && ItemStack.areItemStacksEqual(itemStack, stack))
					{
						return true;
					}
				}
				return false;
			}
		}
	}
}
