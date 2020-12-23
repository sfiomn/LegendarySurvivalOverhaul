package icey.survivaloverhaul.api.temperature;

import com.google.common.collect.ImmutableMap;

public interface ITemperatureCapability
{
	public int getTemperatureLevel();
	public int getTemperatureTickTimer();
	public ImmutableMap<String, TemporaryModifier> getTemporaryModifiers();
	
	public void setTemperatureLevel(int temperature);
	public void setTemperatureTickTimer(int ticktimer);
	public void setTemporaryModifier(String name, float temperature, int duration);
	
	public void addTemperatureLevel(int temperature);
	public void addTemperatureTickTimer(int ticktimer);
	
	public void clearTemporaryModifiers();
}
