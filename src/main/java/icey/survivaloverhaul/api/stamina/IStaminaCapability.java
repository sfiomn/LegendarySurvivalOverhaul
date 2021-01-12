package icey.survivaloverhaul.api.stamina;

public interface IStaminaCapability
{
	public int getStamina();
	public StaminaState getStaminaState();
	
	public void setStamina();
	public void addStamina();
}
