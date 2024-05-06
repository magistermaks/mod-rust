package net.darktree.rust.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.Optional;

public class BlockUtil {

	public static <T extends BlockEntity> Optional<T> getBlockEntity(BlockView world, BlockPos pos, Class<T> clazz) {
		return Optional.ofNullable(clazz.cast(world.getBlockEntity(pos)));
	}

}
