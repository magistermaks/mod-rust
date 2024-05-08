package net.darktree.rust.assembly;

import net.darktree.rust.Rust;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.darktree.rust.util.BlockUtil;
import net.darktree.rust.util.ContainerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public final class AssemblyType {

	@FunctionalInterface
	public interface TooltipSupplier {
		void append(List<Text> tooltip, TooltipContext context);
	}

	private final BlockSoundGroup sounds;
	private final Map<BlockRotation, AssemblyConfig> rotations;
	private final AssemblyInstance.Factory constructor;
	private final TooltipSupplier tooltipSupplier;

	private Identifier identifier = null;
	private String translation = null;

	public AssemblyType(List<BlockPos> blocks, VoxelShape shape, BlockSoundGroup sounds, AssemblyInstance.Factory factory, TooltipSupplier tooltipSupplier) {
		this.rotations = ContainerUtil.enumMapOf(BlockRotation.class, rotation -> new AssemblyConfig(blocks, shape, rotation));
		this.sounds = sounds;
		this.constructor = factory;
		this.tooltipSupplier = tooltipSupplier;

		if (!blocks.contains(BlockPos.ORIGIN)) {
			throw new RuntimeException("Origin of a block assembly not contained within the assembly!");
		}
	}

	public static BlockPos getPlacementPosition(World world, BlockHitResult hit) {
		final int offset = isStateValid(world.getBlockState(hit.getBlockPos())) ? 0 : 1;
		return hit.getBlockPos().offset(hit.getSide(), offset);
	}

	private static boolean isStateValid(BlockState state) {
		return state.isAir() || state.isReplaceable();
	}

	public boolean isValid(World world, BlockPos origin, BlockRotation rotation) {
		BlockPos.Mutable target = new BlockPos.Mutable();
		AssemblyConfig config = rotations.get(rotation);

		if (!world.doesNotIntersectEntities(null, config.getShape(origin))) {
			return false;
		}

		for (BlockPos pos : config.getBlocks()) {
			target.set(pos).move(origin);
			if (!isStateValid(world.getBlockState(target))) {
				return false;
			}
		}

		return true;
	}

	public boolean tryPlace(World world, BlockPos origin, BlockRotation rotation) {
		BlockPos.Mutable target = new BlockPos.Mutable();
		AssemblyConfig config = rotations.get(rotation);
		AssemblyInstance instance = createInstance(rotation, origin);

		for (BlockPos pos : config.getBlocks()) {
			target.set(pos).move(origin);
			world.setBlockState(target, Rust.PART.getDefaultState().with(AssemblyBlock.CENTRAL, pos.equals(BlockPos.ORIGIN)));
			BlockUtil.getBlockEntity(world, target, AssemblyBlockEntity.class).orElseThrow().setAssembly(instance, pos);
		}

		return true;
	}

	public Identifier getIdentifier() {
		if (this.identifier == null) {
			this.identifier = Rust.ASSEMBLY_REGISTRY.getId(this);
		}

		return this.identifier;
	}

	public BlockSoundGroup getSoundGroup() {
		return this.sounds;
	}

	public AssemblyConfig getConfigFor(BlockRotation rotation) {
		return rotations.get(rotation);
	}

	public AssemblyInstance createInstance(BlockRotation rotation, BlockPos origin) {
		return constructor.create(this, rotation, origin);
	}

	public String getTranslationKey() {
		if (this.translation == null) {
			this.translation = Util.createTranslationKey("assembly", getIdentifier());
		}

		return this.translation;
	}

	public void appendTooltip(List<Text> tooltip, TooltipContext context) {
		tooltipSupplier.append(tooltip, context);
	}

}
