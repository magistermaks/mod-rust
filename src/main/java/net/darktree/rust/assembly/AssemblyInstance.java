package net.darktree.rust.assembly;

import net.darktree.rust.Rust;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.entity.DecalPushConstant;
import net.darktree.rust.block.entity.ServerAssemblyDecal;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AssemblyInstance implements AssemblyRenderView {

	@FunctionalInterface
	public interface Factory {
		AssemblyInstance create(AssemblyType type, BlockRotation rotation, BlockPos origin);
	}

	private double velocity = 0, angle = 0;

	private final Map<DecalPushConstant.Type, DecalPushConstant> constants;
	private final Map<BlockPos, List<? extends ServerAssemblyDecal>> decals;

	private final AssemblyType type;
	private final AssemblyConfig config;
	private final BlockRotation rotation;
	private final BlockPos origin;

	public AssemblyInstance(AssemblyType type, BlockRotation rotation, BlockPos origin) {
		this.type = type;
		this.config = type.getConfigFor(rotation);
		this.rotation = rotation;
		this.origin = origin;
		this.constants = type.createConstantMap();
		this.decals = type.createDecalMap();
	}

	public AssemblyInstance(NbtCompound nbt) {
		this.type = Rust.ASSEMBLY_REGISTRY.get(Identifier.tryParse(nbt.getString("id")));
		this.rotation = BlockRotation.values()[nbt.getByte("facing") % 4];
		this.config = Objects.requireNonNull(this.type).getConfigFor(this.rotation);
		this.origin = BlockEntity.posFromNbt(nbt);
		this.constants = type.createConstantMap();
		this.decals = type.createDecalMap();
	}

	public void serialize(NbtCompound nbt) {
		nbt.putString("id", type.getIdentifier().toString());
		nbt.putByte("facing", (byte) rotation.ordinal());
		nbt.putInt("x", origin.getX());
		nbt.putInt("y", origin.getY());
		nbt.putInt("z", origin.getZ());
	}

	@Override
	public DecalPushConstant getDecalPushConstant(DecalPushConstant.Type type) {
		if (constants == null) {
			Rust.LOGGER.warn("Decal push constant was requested before the array was initialized!");
			return type.getFallback();
		}

		DecalPushConstant constant = constants.get(type);

		if (constant == null) {
			return type.getFallback();
		}

		return constant;
	}

	public AssemblyType getType() {
		return type;
	}

	public VoxelShape getShape(BlockPos offset) {
		return config.getShape(offset);
	}

	@Override
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
		velocity += (hit.getSide().getDirection() == Direction.AxisDirection.NEGATIVE ? +12 : -12);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		velocity = (velocity - Math.sin(angle * 0.01745329)) * (1 - 0.21 / MathHelper.clamp(1 + Math.abs(velocity), 1, 3));
		angle = angle + velocity * 1.98;

		constants.get(Rust.CRANK).push(angle);
	}

	public void random(World world, BlockPos pos, BlockState state, Random random) {
		if (!state.get(AssemblyBlock.CENTRAL)) {
			Rust.LOGGER.error("randomTick: Instance ticked by external source! This should not have happened!");
		}
	}

}
