package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;

public class SewingTableBlock extends HorizontalDirectionalBlock implements MenuProvider
{
	private static final Component CONTAINER_TITLE = Component.translatable("container." + LegendarySurvivalOverhaul.MOD_ID + ".sewing_table").withStyle();


	private static final VoxelShape BASE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	private static final VoxelShape X_TOP = Block.box(6.0D, 1.0D, 2.0D, 10.0D, 10.0D, 14.0D);
	private static final VoxelShape Z_TOP = Block.box(2.0D, 1.0D, 6.0D, 14.0D, 10.0D, 10.0D);

	private static final VoxelShape X_AXIS_AABB = Shapes.or(BASE, X_TOP);
	private static final VoxelShape Z_AXIS_AABB = Shapes.or(BASE, Z_TOP);

	public SewingTableBlock()
	{
		super(Properties
				.of()
				.mapColor(MapColor.WOOD)
				.strength(4.0f, 10.0f)
				.noOcclusion());
		
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH));
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		Direction direction = state.getValue(FACING);
		return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(state.getMenuProvider(level, pos));
		return InteractionResult.CONSUME;
	}

	@Override
	public @NotNull Component getDisplayName() {
		return CONTAINER_TITLE;
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return new SewingTableContainer(windowId, playerInventory, ContainerLevelAccess.create(player.level(), player.blockPosition()));
	}
}
