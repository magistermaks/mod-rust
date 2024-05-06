package net.darktree.rust.util;

import net.minecraft.block.Block;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.List;

public class VoxelUtil {

	public static class Builder {

		private final List<VoxelShape> shapes = new ArrayList<>();

		public Builder addCuboid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
			return add(Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ));
		}

		public Builder addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
			return add(Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ));
		}

		public Builder add(VoxelShape shape) {
			shapes.add(shape);
			return this;
		}

		public VoxelShape build() {
			return combine(shapes).simplify();
		}

	}

	public static Builder begin() {
		return new Builder();
	}

	public static VoxelShape combine(List<VoxelShape> shapes) {
		return shapes.stream().reduce(VoxelShapes.empty(), (a, b) -> VoxelShapes.combine(a, b, BooleanBiFunction.OR));
	}

}
