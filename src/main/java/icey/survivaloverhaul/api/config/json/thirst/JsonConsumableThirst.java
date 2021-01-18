package icey.survivaloverhaul.api.config.json.thirst;

import javax.annotation.Nullable;

import icey.survivaloverhaul.api.config.json.JsonItemIdentity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class JsonConsumableThirst
{
	public JsonItemIdentity identity;
	
	public int amount;
	public float saturation;
	public float thirstChance;
	
	public JsonConsumableThirst(int amount, float saturation, float thirstChance, JsonItemIdentity identity)
	{
		this.amount = amount;
		this.saturation = saturation;
		this.thirstChance = thirstChance;
		this.identity = identity;
	}
	
	public boolean matches(ItemStack stack)
	{
		return identity.matches(stack);
	}
	
	public boolean matches(JsonItemIdentity sentIdentity)
	{
		return identity.matches(sentIdentity);
	}
	
	public boolean matches(@Nullable CompoundNBT compound)
	{
		return identity.matches(compound);
	}
}
