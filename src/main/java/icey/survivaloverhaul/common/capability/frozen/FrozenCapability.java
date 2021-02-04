package icey.survivaloverhaul.common.capability.frozen;

import net.minecraft.nbt.CompoundNBT;

public class FrozenCapability
{
	public FrozenCapability()
	{
		this.init();
	}
	
	private void init()
	{
		
	}
	
	public CompoundNBT save() 
	{
		CompoundNBT compound = new CompoundNBT();
		
		return compound;
	}
	
	public void load(CompoundNBT compound)
	{
		this.init();
	}
}
