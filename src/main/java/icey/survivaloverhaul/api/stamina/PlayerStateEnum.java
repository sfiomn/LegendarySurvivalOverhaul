package icey.survivaloverhaul.api.stamina;

public enum PlayerStateEnum
{
	NONE(ClimbingAction.NONE, 0),
	SPRINTING(ClimbingAction.NONE, 10, true);
	
	public final ClimbingAction climbingAction;
	public final int staminaRate;
	public final boolean isConsumed;
	
	private PlayerStateEnum(ClimbingAction climbingAction, int staminaRate)
	{
		this.climbingAction = climbingAction;
		this.staminaRate = staminaRate;
		this.isConsumed = false;
	}
	
	private PlayerStateEnum(ClimbingAction climbingAction, int staminaRate, boolean isConsumed)
	{
		this.climbingAction = climbingAction;
		this.staminaRate = staminaRate;
		this.isConsumed = isConsumed;
	}
	
	public enum ClimbingAction
	{
		NONE,
		SLIDING,
		LEDGE_GRAB,
		CLIMBING,
		LEDGE_CLIMB,
		SLIPPING;
		
		public boolean isClimbing()
		{
			return this != NONE;
		}
	}
}
