package net.darktree.rust.block.entity;

import net.darktree.rust.Rust;
import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.util.DebugAppender;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;

public class AssemblyBlockEntity extends BlockEntity implements DebugAppender {

	private static final String ASSEMBLY_KEY = "assembly";
	private static final String OFFSET_KEY = "offset";
	private static final String MODEL_KEY = "model";

	private AssemblyInstance instance;
	private WeakReference<AssemblyInstance> assembly;
	private BlockPos offset;
	private BlockPos modelOffsetKey;

	public AssemblyBlockEntity(BlockPos pos, BlockState state) {
		super(Rust.ASSEMBLY_BLOCK_ENTITY, pos, state);
		assembly = new WeakReference<>(null);
	}

	public Optional<AssemblyInstance> getAssembly() {
		if (assembly.get() == null && this.offset != null) {
			BlockEntity entity = world.getBlockEntity(getCenter());

			if (entity instanceof AssemblyBlockEntity assemblyEntity) {
				assembly = new WeakReference<>(assemblyEntity.instance);
			}
		}

		return Optional.ofNullable(assembly.get());
	}

	public BlockPos getCenter() {
		return this.pos.add(this.offset);
	}

	public BlockPos getModelOffsetKey() {
		return this.modelOffsetKey;
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		// save the assembly if we are the central block
		if (getCachedState().get(AssemblyBlock.CENTRAL)) {
			NbtCompound instanceNbt = new NbtCompound();
			instance.serialize(instanceNbt);
			nbt.put(ASSEMBLY_KEY, instanceNbt);
		}

		nbt.putLong(OFFSET_KEY, this.offset.asLong());
		nbt.putLong(MODEL_KEY, this.modelOffsetKey.asLong());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		// load the assembly if we are the central block
		if (getCachedState().get(AssemblyBlock.CENTRAL)) {
			this.instance = new AssemblyInstance(nbt.getCompound(ASSEMBLY_KEY));
		}

		this.offset = BlockPos.fromLong(nbt.getLong(OFFSET_KEY));
		this.modelOffsetKey = BlockPos.fromLong(nbt.getLong(MODEL_KEY));
	}

	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	public void setAssembly(@Nullable AssemblyInstance instance, BlockPos pos, BlockPos unrotated) {
		this.instance = instance;
		this.offset = pos.multiply(-1);
		this.modelOffsetKey = unrotated;
		this.assembly = new WeakReference<>(instance);
	}

	public VoxelShape getShape() {
		return getAssembly().map(instance -> instance.getShape(offset)).orElse(VoxelShapes.empty());
	}

	public BlockRotation getRotation() {
		return getAssembly().map(AssemblyInstance::getRotation).orElse(BlockRotation.NONE);
	}

	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		getAssembly().ifPresent(instance -> instance.random(world, pos, state, random));
	}

	public static void worldTick(World world, BlockPos pos, BlockState state, AssemblyBlockEntity entity) {
		entity.getAssembly().ifPresent(instance -> {
//			if (world.isClient()) {
//				instance.clientTick(world, pos, state);
//
//				if (entity.decals != null) {
//					for (ServerAssemblyDecal decal : entity.decals) {
//						decal.tick(world, entity, instance);
//					}
//				}
//			}

			instance.tick(world, pos, state);
		});
	}

	@Override
	public void getDebugReport(World world, BlockPos pos, List<Text> lines) {
		{ // offset
			MutableText ot = Text.literal("Offset: ");

			Text tx = Text.literal(offset.getX() + "").formatted(Formatting.AQUA);
			Text ty = Text.literal(offset.getY() + "").formatted(Formatting.AQUA);
			Text tz = Text.literal(offset.getZ() + "").formatted(Formatting.AQUA);

			Text tm = Text.literal(" (model ").append(modelOffsetKey.toShortString()).append(")").formatted(Formatting.GRAY);

			ot.append(tx).append(", ").append(ty).append(", ").append(tz).append(tm);
			lines.add(ot);
		}

		{ // instance
			MutableText it = Text.literal("Instance: ");
			AssemblyInstance instance = getAssembly().orElse(null);

			if (instance != null) {
				Text tc = Text.literal(instance.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(instance)));
				it.append(Text.literal(Rust.ASSEMBLY_REGISTRY.getId(instance.getType()).toString()).formatted(Formatting.AQUA));
				it.append(" (").append(tc).append(")");
			} else {
				it.append(Text.literal("This assembly has no instance").formatted(Formatting.RED));
			}

			it.append(" ").append(Text.literal("[ins]").formatted(this.instance == null ? Formatting.RED : Formatting.GREEN));
			it.append(" ").append(Text.literal("[lnk]").formatted(this.assembly.get() == null ? Formatting.RED : Formatting.GREEN));

			lines.add(it);

			if (instance != null) {
				instance.getDebugReport(world, modelOffsetKey, lines);
			}
		}
	}

}
