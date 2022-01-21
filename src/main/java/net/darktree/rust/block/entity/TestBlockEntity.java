package net.darktree.rust.block.entity;

import net.darktree.rust.Initializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestBlockEntity extends BlockEntity {

	public TestBlockEntity(BlockPos pos, BlockState state) {
		super(Initializer.TEST_BLOCK_ENTITY, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, TestBlockEntity be) {
//		world.setBlockState(pos.up(2), Blocks.PINK_CONCRETE.getDefaultState());
	}

}
