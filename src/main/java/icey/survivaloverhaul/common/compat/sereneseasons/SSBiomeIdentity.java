package icey.survivaloverhaul.common.compat.sereneseasons;

import com.google.gson.annotations.SerializedName;

// Since the Serene Seasons API doesn't give us access to what biomes are tropical or not
// we have to work around this weird and kinda arbitrary limitation

// Thankfully we don't have to do any file writing for these configs, we just have to 
// read them
public class SSBiomeIdentity
{	
	@SerializedName("enable_seasonal_effects")
	public boolean seasonEffects;
	
	@SerializedName("use_tropical_seasons")
	public boolean isTropical;
	
	public SSBiomeIdentity(boolean isTropical)
	{
		this(true, isTropical);
	}
	
	public SSBiomeIdentity(boolean seasonEffects, boolean isTropical)
	{
		this.seasonEffects = seasonEffects;
		this.isTropical = isTropical;
	}
}
