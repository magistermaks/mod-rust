package net.darktree.rust.render;

import net.minecraft.util.BlockRotation;

public enum Rotation {

	NORTH(0, 0, 0, BlockRotation.NONE),
	WEST(0, 1, 90, BlockRotation.COUNTERCLOCKWISE_90),
	SOUTH(1, 1, 180, BlockRotation.CLOCKWISE_180),
	EAST(1, 0, 270, BlockRotation.CLOCKWISE_90);

	public final float x, z, angle;
	private final BlockRotation rotation;

	Rotation(float x, float z, float angle, BlockRotation rotation) {
		this.x = x;
		this.z = z;
		this.angle = angle;
		this.rotation = rotation;
	}

	public Rotation next() {
		return values()[(ordinal() + 1) % values().length];
	}

	public BlockRotation getBlockRotation() {
		return rotation;
	}

}
