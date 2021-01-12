package icey.survivaloverhaul.api.stamina;

public enum StaminaState
{
	NORMAL(15),
	EXHAUSTED(0),
	RECOVERING(10);
	
	private int recoveryRate;
	
	private StaminaState(int recoveryRate)
	{
		this.recoveryRate = recoveryRate;
	}
	
	public int getRecoveryRate()
	{
		return this.recoveryRate;
	}
}
