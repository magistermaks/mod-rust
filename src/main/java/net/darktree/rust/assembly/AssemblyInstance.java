package net.darktree.rust.assembly;

import net.darktree.rust.Rust;
import net.darktree.rust.RustRegistries;
import net.darktree.rust.assembly.decal.DecalPushConstant;
import net.darktree.rust.assembly.decal.ServerAssemblyDecal;
import net.darktree.rust.util.DebugAppender;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AssemblyInstance implements AssemblyRenderView, DebugAppender {

	@FunctionalInterface
	public interface Factory {
		AssemblyInstance create(BlockRotation rotation, BlockPos origin);
	}

	protected final Map<DecalPushConstant.Type, DecalPushConstant> constants;
	protected final Map<BlockPos, List<? extends ServerAssemblyDecal>> decals;

	protected final AssemblyType type;
	protected final AssemblyConfig config;
	protected final BlockRotation rotation;
	protected final BlockPos origin;

	public AssemblyInstance(AssemblyType type, BlockRotation rotation, BlockPos origin) {
		this.type = type;
		this.config = type.getConfigFor(rotation);
		this.rotation = rotation;
		this.origin = origin;
		this.constants = type.createConstantMap();
		this.decals = type.createDecalMap();
	}

	public static AssemblyInstance deserialize(NbtCompound nbt) {
		AssemblyType type = RustRegistries.ASSEMBLY.get(Identifier.tryParse(nbt.getString("id")));
		BlockRotation rotation = BlockRotation.values()[nbt.getByte("facing") % 4];
		BlockPos origin = BlockEntity.posFromNbt(nbt);

		AssemblyInstance instance = Objects.requireNonNull(type).createInstance(rotation, origin);
		instance.fromNbt(nbt);
		return instance;
	}

	public void fromNbt(NbtCompound nbt) {

	}

	public void toNbt(NbtCompound nbt) {
		nbt.putString("id", type.getIdentifier().toString());
		nbt.putByte("facing", (byte) rotation.ordinal());
		nbt.putInt("x", origin.getX());
		nbt.putInt("y", origin.getY());
		nbt.putInt("z", origin.getZ());
	}

	@Override
	public final DecalPushConstant getDecalPushConstant(DecalPushConstant.Type type) {
		DecalPushConstant constant = constants.get(type);
		return constant == null ? type.getFallback() : constant;
	}

	@Override
	public void getDebugReport(World world, BlockPos pos, List<Text> lines) {

		// facing
		lines.add(Text.literal("Rotation: ").append(Text.literal(switch (rotation) {
			case NONE -> "0 deg";
			case CLOCKWISE_90 -> "90 deg";
			case CLOCKWISE_180 -> "180 deg";
			case COUNTERCLOCKWISE_90 -> "270 deg";
		}).formatted(Formatting.AQUA)));

		// decal push constants
		lines.add(Text.literal("Constants: ").append(constants.values().stream()
				.map(constant -> Text.literal(RustRegistries.CONSTANT.getId(constant.getType()).toString()).append("=" + constant.getCurrent()).formatted(Formatting.AQUA))
				.reduce((a, b) -> Text.literal("").append(a).append(", ").append(b))
				.orElseGet(() -> Text.literal("This instance has no constants").formatted(Formatting.GRAY))
		));

		// decals
		lines.add(Text.literal("Decals: ").append(decals.getOrDefault(pos, List.of()).stream()
				.map(decal -> Text.literal(RustRegistries.DECAL.getId(decal.getType()).toString()).formatted(Formatting.AQUA))
				.reduce((a, b) -> Text.literal("").append(a).append(", ").append(b))
				.orElseGet(() -> Text.literal("This block has no instance decals").formatted(Formatting.GRAY))
		));

	}

	@Override
	public BlockRotation getRotation() {
		return rotation;
	}

	public final AssemblyType getType() {
		return type;
	}

	public final VoxelShape getWholeShape(BlockPos offset) {
		return config.getWholeShape(offset);
	}
	
	public final AssemblyConfig getConfig() {
		return config;
	}

	public final BlockPos getOrigin() {
		return origin;
	}

	public void onRemoved(World world) {

	}

	public List<? extends ServerAssemblyDecal> getDecalList(BlockPos pos) {
		return decals.computeIfAbsent(pos, key -> List.of());
	}

	public void appendDrops(List<ItemStack> stacks) {
		stacks.add(asItem());
	}

	public ItemStack asItem() {
		return new ItemStack(Rust.TEST_ITEM);
	}

	public void onUse(World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

	}

	public void tick(World world, BlockPos pos, BlockState state) {

	}

	public void random(World world, BlockPos pos, BlockState state, Random random) {

	}

}
