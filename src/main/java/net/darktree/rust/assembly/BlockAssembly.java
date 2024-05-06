package net.darktree.rust.assembly;

import com.google.common.collect.ImmutableList;
import net.darktree.rust.Rust;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.darktree.rust.util.BlockUtil;
import net.darktree.rust.util.ContainerUtil;
import net.darktree.rust.util.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class BlockAssembly {

	private final BlockSoundGroup sounds;
	private final Map<BlockRotation, AssemblyConfig> rotations;

	public BlockAssembly(List<BlockPos> blocks, VoxelShape shape, BlockSoundGroup sounds) {
		this.rotations = ContainerUtil.enumMapOf(BlockRotation.class, rotation -> new AssemblyConfig(blocks, shape, rotation));
		this.sounds = sounds;
	}

	public static BlockPos getPlacementPosition(World world, BlockHitResult hit) {
		final int offset = isStateValid(world.getBlockState(hit.getBlockPos())) ? 0 : 1;
		return hit.getBlockPos().offset(hit.getSide(), offset);
	}

	private static boolean isStateValid(BlockState state) {
		return state.isAir() || state.isReplaceable();
	}

	public boolean isValid(World world, BlockPos origin, BlockRotation rotation) {
		BlockPos.Mutable target = new BlockPos.Mutable();
		AssemblyConfig config = rotations.get(rotation);

		if (!world.doesNotIntersectEntities(null, config.getShape(origin))) {
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

	public void place(World world, BlockPos origin, BlockRotation rotation) {
		BlockPos.Mutable target = new BlockPos.Mutable();
		AssemblyConfig config = rotations.get(rotation);

		for (BlockPos pos : config.getBlocks()) {
			target.set(pos).move(origin);
			world.setBlockState(target, Rust.TEST.getDefaultState().with(AssemblyBlock.CENTRAL, pos.equals(BlockPos.ORIGIN)));
			BlockUtil.getBlockEntity(world, target, AssemblyBlockEntity.class).orElseThrow().setAssembly(this, rotation, pos);
		}
	}

	public BlockSoundGroup getSoundGroup() {
		return this.sounds;
	}

	public AssemblyConfig getConfigFor(BlockRotation rotation) {
		return rotations.get(rotation);
	}

}
