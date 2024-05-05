package net.darktree.rust.render;

import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public enum Rotation {

	NORTH(0, 0, 0, Direction.NORTH, BlockRotation.NONE),
	WEST(0, 1, 90, Direction.WEST, BlockRotation.COUNTERCLOCKWISE_90),
	SOUTH(1, 1, 180, Direction.SOUTH, BlockRotation.CLOCKWISE_180),
	EAST(1, 0, 270, Direction.EAST, BlockRotation.CLOCKWISE_90);

	public final float x, z, angle;
	public final Direction direction;
	public final BlockRotation rotation;

	Rotation(float x, float z, float angle, Direction direction, BlockRotation rotation) {
		this.x = x;
		this.z = z;
		this.angle = angle;
		this.direction = direction;
		this.rotation = rotation;
	}

	public Rotation next() {
		return values()[(ordinal() + 1) % values().length];
	}

}
