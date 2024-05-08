package net.darktree.rust.block;

import net.darktree.rust.Rust;
import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.darktree.rust.util.BlockUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class AssemblyBlock extends Block implements BlockEntityProvider {

	public static final BooleanProperty CENTRAL = BooleanProperty.of("central");

	public AssemblyBlock(Settings settings) {
		super(settings);
		super.setDefaultState(getDefaultState().with(CENTRAL, false));
	}

	public Optional<AssemblyInstance> getAssembly(BlockView world, BlockPos pos) {
		return BlockUtil.getBlockEntity(world, pos, AssemblyBlockEntity.class).flatMap(AssemblyBlockEntity::getAssembly);
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
	public void onStateReplaced(BlockState state, World world, BlockPos origin, BlockState newState, boolean moved) {
		getAssembly(world, origin).ifPresent(instance -> {
			BlockPos.Mutable pos = new BlockPos.Mutable();
			instance.onRemoved(world);

			for (BlockPos offset : instance.getConfig().getBlocks()) {
				pos.set(offset).move(instance.getOrigin());

				BlockState target = world.getBlockState(pos);

				if (target.getBlock() == Rust.PART) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
			}
		});

		// this removes the block entity
		super.onStateReplaced(state, world, origin, newState, moved);
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
		List<ItemStack> stacks = super.getDroppedStacks(state, builder);

		if (builder.get(LootContextParameters.BLOCK_ENTITY) instanceof AssemblyBlockEntity assemblyEntity) {
			assemblyEntity.getAssembly().ifPresent(instance -> {
				instance.appendDrops(stacks);
			});
		}

		return stacks;
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		ItemStack stack = ItemStack.EMPTY;

		Optional<AssemblyInstance> instance = getAssembly(world, pos);

		if (instance.isPresent()) {
			stack = instance.get().asItem();
		}

		return stack;
	}

}
