package net.darktree.rust.assembly;

import net.darktree.rust.Rust;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.Objects;

public class AssemblyReference {

	private final BlockAssembly assembly;
	private final BlockRotation rotation;
	private final AssemblyConfig config;
	private final BlockPos offset;

	public AssemblyReference(BlockAssembly assembly, BlockRotation rotation, BlockPos offset) {
		this.assembly = assembly;
		this.rotation = rotation;
		this.config = assembly.getConfigFor(rotation);
		this.offset = offset.multiply(-1);
	}

	public AssemblyReference(NbtCompound nbt) {
		this.assembly = Rust.ASSEMBLY_REGISTRY.get(Identifier.tryParse(nbt.getString("assembly")));
		this.rotation = BlockRotation.values()[nbt.getByte("rotation") % 4];
		this.config = Objects.requireNonNull(this.assembly).getConfigFor(this.rotation);
		this.offset = BlockPos.fromLong(nbt.getLong("offset"));
	}

	public void serialize(NbtCompound nbt) {
		nbt.putString("assembly", Rust.ASSEMBLY_REGISTRY.getId(assembly).toString());
		nbt.putByte("rotation", (byte) rotation.ordinal());
		nbt.putLong("offset", this.offset.asLong());
	}

	public VoxelShape getShape() {
		return config.getShape(this.offset);
	}

	public BlockRotation getRotation() {
		return rotation;
	}

}
