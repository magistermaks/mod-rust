package net.darktree.rust.block.entity;

import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public final class DecalType <T extends ServerAssemblyDecal> {

	private final Function<DecalType<T>, T> constructor;

	public DecalType(Function<DecalType<T>, T> supplier) {
		this.constructor = supplier;
	}

	public T create() {
		return constructor.apply(this);
	}

	public DecalPlacement<T> at(BlockPos pos) {
		return new DecalPlacement<>(this, pos);
	}

	public DecalPlacement<T> at(int x, int y, int z) {
		return at(new BlockPos(x, y, z));
	}

}
