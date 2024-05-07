package net.darktree.rust.assembly;

import net.darktree.rust.Rust;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class AssemblyInstance {

	@FunctionalInterface
	public interface Factory {
		AssemblyInstance create(AssemblyType type, BlockRotation rotation, BlockPos origin);
	}

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
		nbt.putString("id", type.getIdentifier().toString());
		nbt.putByte("facing", (byte) rotation.ordinal());
		nbt.putInt("x", origin.getX());
		nbt.putInt("y", origin.getY());
		nbt.putInt("z", origin.getZ());
	}

	public AssemblyType getType() {
		return type;
	}

	public VoxelShape getShape(BlockPos offset) {
		return config.getShape(offset);
	}

	public BlockRotation getRotation() {
		return rotation;
	}

	public void onRemoved(World world) {

	}

	public AssemblyConfig getConfig() {
		return config;
	}

	public BlockPos getOrigin() {
		return origin;
	}

	public void appendDrops(List<ItemStack> stacks) {
		stacks.add(new ItemStack(Rust.TEST_ITEM));
	}

}
