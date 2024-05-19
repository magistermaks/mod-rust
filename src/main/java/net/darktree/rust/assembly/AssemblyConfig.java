package net.darktree.rust.assembly;

import com.google.common.collect.ImmutableList;
import net.darktree.rust.util.RotationUtil;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

import java.util.List;

public class AssemblyConfig {

	private final List<BlockPair> blocks;
	private final VoxelShape shape;

	AssemblyConfig(List<BlockPos> blocks, VoxelShape shape, BlockRotation rotation) {
		this.blocks = ImmutableList.copyOf(blocks.stream().map(pos -> BlockPair.of(rotation, pos)).toList());
		this.shape = RotationUtil.rotateVoxelShape(shape, rotation);
	}

	public List<BlockPair> getBlocks() {
		return blocks;
	}

	public VoxelShape getShape(BlockPos offset) {
		return shape.offset(offset.getX(), offset.getY(), offset.getZ());
	}

	public record BlockPair(BlockPos offset, BlockPos key) {

		public static BlockPair of(BlockRotation rotation, BlockPos pos) {
			return new BlockPair(pos.rotate(rotation), pos);
		}

	}

}
