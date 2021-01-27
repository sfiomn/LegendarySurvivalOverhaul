package icey.survivaloverhaul.api.stamina;

public enum PlayerState
{
	IDLE("idle", StaminaAction.RECOVER),
	MIDAIR("midair", StaminaAction.NO_CHANGE),
	RUNNING("running", StaminaAction.FASTEST_CONSUME),
	SWIMMING("swimming", StaminaAction.FASTER_CONSUME),
	
	FREE_CLIMBING("free_climbing", StaminaAction.SLOW_CONSUME, ClimbingState.FREE_CLIMBING),
	HANGING("hanging", StaminaAction.NO_CHANGE, ClimbingState.HANGING),
	LADDER_CLIMBING("ladder_climbing", StaminaAction.NO_CHANGE, ClimbingState.LADDER_CLIMBING),
	LEDGE_GRAB("ledge_grab", StaminaAction.NO_CHANGE, ClimbingState.LEDGE_GRAB),
	LEDGE_PULLUP("ledge_pullup", StaminaAction.SLOW_CONSUME, ClimbingState.LEDGE_PULLUP),
	SLIPPING("slipping", StaminaAction.FASTER_CONSUME, ClimbingState.SLIPPING),
	WALL_GRAB("wall_grab", StaminaAction.NO_CHANGE, ClimbingState.WALL_GRAB),
	JUMP_UP("jump_up", StaminaAction.FASTEST_CONSUME, ClimbingState.JUMP_UP),
	WALL_JUMP("wall_jump", StaminaAction.FASTER_CONSUME, ClimbingState.WALL_JUMP);
	
	public String name;
	public StaminaAction action;
	public ClimbingState climbingState;
	
	private PlayerState(String name)
	{
		this(name, StaminaAction.NO_CHANGE, ClimbingState.NONE);
	}
	
	private PlayerState(String name, StaminaAction action)
	{
		this(name, action, ClimbingState.NONE);
	}
	
	private PlayerState(String name, StaminaAction action, ClimbingState climbingState)
	{
		this.name = name;
		this.action = action;
		this.climbingState = climbingState;
	}
	
	public boolean isClimbing()
	{
		return climbingState.isClimbing();
	}
	
	public static PlayerState getStateFromString(String str)
	{
		for (PlayerState state : PlayerState.values())
		{
			if (str.equalsIgnoreCase(state.name))
			{
				return state;
			}
		}
		
		return IDLE;
	}
	
	public enum ClimbingState
	{
		NONE,
		FREE_CLIMBING, // Facing towards the wall and actively moving up, down, and to the sides
		HANGING, // Facing towards the wall but not upwards
		LADDER_CLIMBING,
		LEDGE_GRAB,
		LEDGE_PULLUP,
		SLIPPING,
		WALL_GRAB,
		JUMP_UP,
		WALL_JUMP;
		
		public boolean isClimbing()
		{
			return this != NONE && this != WALL_JUMP;
		}
		
	}
	
	public enum StaminaAction
	{
		NO_CHANGE(0, false),
		FASTEST_CONSUME(3, true),
		FASTER_CONSUME(2, true),
		SLOW_CONSUME(1, true),
		SLOW_RECOVER(1, false),
		RECOVER(2, false);
		
		public final int change;
		public final boolean consumed;
		
		private StaminaAction(int change, boolean consumed)
		{
			this.change = change;
			this.consumed = consumed;
		}
	}
	
	
}
