package net.darktree.rust.block.entity;

import net.minecraft.util.math.BlockPos;

public class DecalPlacement <T extends ServerAssemblyDecal> {

	private final DecalType<T> type;
	private final BlockPos pos;

	DecalPlacement(DecalType<T> type, BlockPos pos) {
		this.type = type;
		this.pos = pos;
	}

	public DecalType<T> getType() {
		return type;
	}

	public BlockPos getOffset() {
		return pos;
	}

}
