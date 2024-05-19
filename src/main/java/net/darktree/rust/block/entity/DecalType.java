package net.darktree.rust.block.entity;

import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public final class DecalType <T extends ServerAssemblyDecal> {

	private final Supplier<T> constructor;

	public DecalType(Supplier<T> supplier) {
		constructor = supplier;
	}

	public T create() {
		return constructor.get();
	}

	public DecalPlacement<T> at(BlockPos pos) {
		return new DecalPlacement<>(this, pos);
	}

	public DecalPlacement<T> at(int x, int y, int z) {
		return at(new BlockPos(x, y, z));
	}

}
