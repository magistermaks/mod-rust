package net.darktree.rust.render;

import net.minecraft.util.math.Direction;

public enum Rotation {

	NORTH(0, 0, 0, Direction.NORTH),
	WEST(0, 1, 90, Direction.WEST),
	SOUTH(1, 1, 180, Direction.SOUTH),
	EAST(1, 0, 270, Direction.EAST);

	public final float x, z, angle;
	public final Direction direction;

	Rotation(float x, float z, float angle, Direction direction) {
		this.x = x;
		this.z = z;
		this.angle = angle;
		this.direction = direction;
	}

	public Rotation next() {
		return values()[(ordinal() + 1) % values().length];
	}

}
