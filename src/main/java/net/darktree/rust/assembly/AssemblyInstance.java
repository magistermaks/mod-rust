package net.darktree.rust.assembly;

import net.darktree.rust.Rust;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.Objects;

public class AssemblyInstance {

	private final AssemblyType type;
	private final AssemblyConfig config;
	private final BlockRotation rotation;
	private final BlockPos origin;

	public AssemblyInstance(AssemblyType type, BlockRotation rotation, BlockPos origin) {
		this.type = type;
		this.config = type.getConfigFor(rotation);
		this.rotation = rotation;
		this.origin = origin;
	}

	public AssemblyInstance(NbtCompound nbt) {
		this.type = Rust.ASSEMBLY_REGISTRY.get(Identifier.tryParse(nbt.getString("id")));
		this.rotation = BlockRotation.values()[nbt.getByte("facing") % 4];
		this.config = Objects.requireNonNull(this.type).getConfigFor(this.rotation);
		this.origin = BlockEntity.posFromNbt(nbt);
	}

	public void serialize(NbtCompound nbt) {
		nbt.putString("id", Rust.ASSEMBLY_REGISTRY.getId(type).toString());
		nbt.putByte("facing", (byte) rotation.ordinal());
		nbt.putInt("x", origin.getX());
		nbt.putInt("y", origin.getY());
		nbt.putInt("z", origin.getZ());
	}

	public VoxelShape getShape(BlockPos offset) {
		return config.getShape(offset);
	}

	public BlockRotation getRotation() {
		return rotation;
	}

	public void onBreak(World world) {

	}

	public AssemblyConfig getConfig() {
		return config;
	}

	public BlockPos getOrigin() {
		return origin;
	}
}
