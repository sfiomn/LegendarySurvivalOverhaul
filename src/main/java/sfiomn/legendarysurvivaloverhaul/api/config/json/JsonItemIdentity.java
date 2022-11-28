package sfiomn.legendarysurvivaloverhaul.api.config.json;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import javax.annotation.Nullable;

/**
 * Code taken and adapted from Charles445's SimpleDifficulty mod
 * @see <a href="https://github.com/Charles445/SimpleDifficulty/tree/master/src/main/java/com/charles445/simpledifficulty/api/config/json">Github Link</a>
 * @author Charles445
 * @author Icey
 */

public class JsonItemIdentity
{
	@Nullable
	public String nbt;
	
	@Nullable
	private CompoundNBT compound;
	
	public JsonItemIdentity(String nbt)
	{
		this.nbt = nbt;
		
		tryPopulateCompound();
	}
	
	public void tryPopulateCompound()
	{
		if(this.compound == null)
		{
			if (this.nbt == null)
			{
				this.compound = null;
			}
			else
			{
				try
				{
					this.compound = JsonToNBT.parseTag(nbt);

				}
				catch (Exception e)
				{
					LegendarySurvivalOverhaul.LOGGER.error("An error occured while populating NBT from json data: " + e.getMessage());
					
					this.compound = null;
					this.nbt = null;
				}
			}
		}
	}
	
	public boolean matches(ItemStack stack)
	{
		if(stack.hasTag())
		{
			return matches(stack.getTag());
		}
		else
		{
			return matches((CompoundNBT) null);
		}
	}
	
	public boolean matches(JsonItemIdentity sentIdentity)
	{
		return matches(sentIdentity.compound);
	}
	
	public boolean matches(@Nullable CompoundNBT stackCompound)
	{
		if (nbt == null || nbt.isEmpty())
		{
			return true;
		}
		else
		{
			tryPopulateCompound();
			
			return checkCompounds(this.compound, stackCompound);
		}
	}
	
	private boolean checkCompounds(CompoundNBT selfCompound, CompoundNBT stackCompound)
	{
		if (stackCompound == null)
		{
			return false;
		}
		
		for (String key : selfCompound.getAllKeys())
		{
			INBT a = selfCompound.get(key);
			INBT b = stackCompound.get(key);
			
			if (a != null)
			{
				if (a instanceof CompoundNBT && b instanceof CompoundNBT)
				{
					if(checkCompounds((CompoundNBT) a, (CompoundNBT) b))
					{
						continue;
					}
					else
					{
						return false;
					}
				}
				else if(a.equals(b))
				{
					continue;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if (b == null)
				{
					// Matching null
					continue;
				}
				else
				{
					return false;
				}
			}
		}
		
		return true;
	}
}
