package net.darktree.rust.block.entity;

import net.darktree.rust.Rust;
import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.block.AssemblyBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.Optional;

public class AssemblyBlockEntity extends BlockEntity {

	private static final String ASSEMBLY_KEY = "assembly";
	private static final String OFFSET_KEY = "offset";

	private AssemblyInstance assembly;
	private BlockPos offset;

	public AssemblyBlockEntity(BlockPos pos, BlockState state) {
		super(Rust.ASSEMBLY_BLOCK_ENTITY, pos, state);
	}

	public Optional<AssemblyInstance> getAssembly() {
		if (assembly == null && this.offset != null) {
			BlockEntity entity = world.getBlockEntity(this.pos.add(this.offset));

			if (entity instanceof AssemblyBlockEntity assemblyEntity) {
				assembly = assemblyEntity.assembly;
			}
		}

		return Optional.ofNullable(assembly);
	}

	public BlockPos getCenter() {
		return this.pos.add(this.offset);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		// save the assembly if we are the central block
		if (getCachedState().get(AssemblyBlock.CENTRAL)) {
			NbtCompound instanceNbt = new NbtCompound();
			assembly.serialize(instanceNbt);
			nbt.put(ASSEMBLY_KEY, instanceNbt);
		}

		nbt.putLong(OFFSET_KEY, this.offset.asLong());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		// save the assembly if we are the central block
		if (getCachedState().get(AssemblyBlock.CENTRAL)) {
			this.assembly = new AssemblyInstance(nbt.getCompound(ASSEMBLY_KEY));
		}

		this.offset = BlockPos.fromLong(nbt.getLong(OFFSET_KEY));
	}

	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	public void setAssembly(AssemblyInstance instance, BlockPos pos) {
		this.assembly = instance;
		this.offset = pos.multiply(-1);
	}

	public VoxelShape getShape() {
		return getAssembly().map(instance -> instance.getShape(offset)).orElse(VoxelShapes.empty());
	}

	public BlockRotation getRotation() {
		return getAssembly().map(AssemblyInstance::getRotation).orElse(BlockRotation.NONE);
	}

}
