package net.darktree.rust.block;

import net.darktree.rust.Rust;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.darktree.rust.util.BlockUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AssemblyBlock extends Block implements BlockEntityProvider {

	public static final BooleanProperty CENTRAL = BooleanProperty.of("central");

	public AssemblyBlock(Settings settings) {
		super(settings);
		super.setDefaultState(getDefaultState().with(CENTRAL, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(CENTRAL);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AssemblyBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return BlockUtil.getBlockEntity(world, pos, AssemblyBlockEntity.class).map(AssemblyBlockEntity::getShape).orElse(VoxelShapes.empty());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getCollisionShape(state, world, pos, context);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onBreak(World world, BlockPos origin, BlockState state, PlayerEntity player) {
		super.onBreak(world, origin, state, player);

		BlockUtil.getBlockEntity(world, origin, AssemblyBlockEntity.class).flatMap(AssemblyBlockEntity::getAssembly).ifPresent(instance -> {
			BlockPos.Mutable pos = new BlockPos.Mutable();
			instance.onBreak(world);

			for (BlockPos offset : instance.getConfig().getBlocks()) {
				pos.set(offset).move(instance.getOrigin());

				BlockState target = world.getBlockState(pos);

				if (target.getBlock() == Rust.PART) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
			}
		});
	}

}
