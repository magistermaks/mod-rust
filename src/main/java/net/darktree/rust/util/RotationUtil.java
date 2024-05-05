package net.darktree.rust.util;

import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

/**
 * Based on the work by Falkreon for United Manufacturing. <br>
 * Licensed under the MIT license.
 * @see <a href="https://github.com/CottonMC/UnitedManufacturing/blob/master/src/main/java/io/github/cottonmc/um/VoxelMath.java">VoxelMath.java</a>
 * @see <a href="https://discord.com/channels/507304429255393322/507982478276034570/577606844722184229">Fabric Discord</a>
 */

public class RotationUtil {

	/**
	 * Rotates the given Vec3D around the Y axis
	 * by the given number of degrees
	 */
	public static Vec3d rotateHorizontally(Vec3d vec, int degrees) {
		double dx = vec.x - 0.5;
		double dz = vec.z - 0.5;
		double theta = Math.atan2(dz, dx);
		double r = Math.sqrt(dx*dx + dz*dz);

		theta += (degrees * (Math.PI/180.0));

		double xp = Math.cos(theta)*r + 0.5;
		double zp = Math.sin(theta)*r + 0.5;

		return new Vec3d(xp, vec.y, zp);
	}

	/**
	 * Rotates the given Box around the Y axis in
	 * 90 degree increments as dictated by the BlockRotation enum
	 */
	public static Box rotateBox(Box box, BlockRotation rotation) {
		Vec3d point1 = new Vec3d(box.minX, box.minY, box.minZ);
		Vec3d point2 = new Vec3d(box.maxX, box.maxY, box.maxZ);

		switch(rotation) {

			case CLOCKWISE_90:
				point1 = rotateHorizontally(point1, 90);
				point2 = rotateHorizontally(point2, 90);
				break;

			case CLOCKWISE_180:
				point1 = rotateHorizontally(point1, 180);
				point2 = rotateHorizontally(point2, 180);
				break;

			case COUNTERCLOCKWISE_90:
				point1 = rotateHorizontally(point1, 270);
				point2 = rotateHorizontally(point2, 270);
				break;

			default:
				break;

		}

		return new Box(point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ());
	}

	/**
	 * Rotates the given Voxel Shape around the Y axis in
	 * 90 degree increments as dictated by the BlockRotation enum
	 */
	public static VoxelShape rotateVoxelShape(VoxelShape shape, BlockRotation rotation) {
		return shape.getBoundingBoxes().stream().map(box -> rotateBox(box, rotation)).map(VoxelShapes::cuboid).reduce(VoxelShapes.empty(), VoxelShapes::union);
	}

}