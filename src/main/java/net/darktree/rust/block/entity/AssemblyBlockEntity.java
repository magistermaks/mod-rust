package net.darktree.rust.block.entity;

import net.darktree.rust.assembly.AssemblyReference;
import net.darktree.rust.assembly.BlockAssembly;
import net.darktree.rust.Rust;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

public class AssemblyBlockEntity extends BlockEntity {

	private AssemblyReference assembly;

	public AssemblyBlockEntity(BlockPos pos, BlockState state) {
		super(Rust.ASSEMBLY_BLOCK_ENTITY, pos, state);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		assembly.serialize(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.assembly = new AssemblyReference(nbt);
	}

	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	public void setAssembly(BlockAssembly assembly, BlockRotation rotation, BlockPos pos) {
		this.assembly = new AssemblyReference(assembly, rotation, pos);
	}

	public VoxelShape getShape() {
		return assembly.getShape();
	}

	public BlockRotation getRotation() {
		return assembly.getRotation();
	}

}
