package net.darktree.rust;

import com.google.common.collect.ImmutableList;
import net.darktree.rust.util.ContainerUtil;
import net.darktree.rust.util.RotationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class BlockAssembly {

	private static class Configuration {

		private final List<BlockPos> blocks;
		private final VoxelShape shape;

		Configuration(List<BlockPos> blocks, VoxelShape shape, BlockRotation rotation) {
			this.blocks = ImmutableList.copyOf(blocks.stream().map(pos -> pos.rotate(rotation)).toList());
			this.shape = RotationUtil.rotateVoxelShape(shape, rotation);
		}

		public List<BlockPos> getBlocks() {
			return blocks;
		}

	}

	private final Map<BlockRotation, Configuration> rotations;

	public BlockAssembly(List<BlockPos> blocks, VoxelShape shape) {
		rotations = ContainerUtil.enumMapOf(BlockRotation.class, rotation -> new Configuration(blocks, shape, rotation));
	}

	public boolean isValid(World world, BlockPos origin, BlockRotation rotation) {
		if (rotation == null) {
			Rust.LOGGER.info("rotation");
		}

		BlockPos.Mutable target = new BlockPos.Mutable();
		Configuration config = rotations.get(rotation);

		if (!world.doesNotIntersectEntities(null, config.shape.offset(origin.getX(), origin.getY(), origin.getZ()))) {
			return false;
		}

		for (BlockPos pos : config.getBlocks()) {
			target.set(pos).move(origin);
			if (!isStateValid(world.getBlockState(target))) {
				return false;
			}
		}

		return true;
	}

	public BlockSoundGroup getSoundGroup() {
		return BlockSoundGroup.ANVIL;
	}

	private boolean isStateValid(BlockState state) {
		return state.isAir() || state.isReplaceable();
	}

}
