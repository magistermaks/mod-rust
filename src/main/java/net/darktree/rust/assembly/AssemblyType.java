package net.darktree.rust.assembly;

import net.darktree.rust.Rust;
import net.darktree.rust.RustRegistries;
import net.darktree.rust.assembly.decal.AssemblyDecalManager;
import net.darktree.rust.assembly.decal.ConfiguredDecal;
import net.darktree.rust.assembly.decal.DecalPushConstant;
import net.darktree.rust.assembly.decal.ServerAssemblyDecal;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.AssemblyBlockEntity;
import net.darktree.rust.util.BlockUtil;
import net.darktree.rust.util.ContainerUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AssemblyType {

	@FunctionalInterface
	public interface TooltipSupplier {
		void append(List<Text> tooltip, TooltipContext context);
	}

	private final Set<DecalPushConstant.Type> constants;
	private final BlockSoundGroup sounds;
	private final Map<BlockRotation, AssemblyConfig> rotations;
	private final AssemblyInstance.Factory constructor;
	private final TooltipSupplier tooltipSupplier;

	// cashes
	private Map<BlockPos, List<? extends ServerAssemblyDecal>> decals;
	private Identifier identifier = null;
	private String translation = null;

	public AssemblyType(Set<DecalPushConstant.Type> constants, List<BlockPos> blocks, VoxelShape shape, BlockSoundGroup sounds, AssemblyInstance.Factory factory, TooltipSupplier tooltipSupplier) {
		this.constants = constants;
		this.rotations = ContainerUtil.enumMapOf(BlockRotation.class, rotation -> new AssemblyConfig(blocks, shape, rotation));
		this.sounds = sounds;
		this.constructor = factory;
		this.tooltipSupplier = tooltipSupplier;

		if (!blocks.contains(BlockPos.ORIGIN)) {
			throw new RuntimeException("Origin of a block assembly not contained within the assembly!");
		}
	}

	public Map<DecalPushConstant.Type, DecalPushConstant> createConstantMap() {
		Map<DecalPushConstant.Type, DecalPushConstant> map = new HashMap<>();
		constants.forEach(constant -> map.put(constant, constant.create()));
		return map;
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

		if (!world.doesNotIntersectEntities(null, config.getWholeShape(origin))) {
			return false;
		}

		for (AssemblyConfig.BlockPair pair : config.getBlocks()) {
			target.set(pair.offset()).move(origin);
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

		for (AssemblyConfig.BlockPair pair : config.getBlocks()) {
			target.set(pair.offset()).move(origin);
			boolean central = pair.offset().equals(BlockPos.ORIGIN);

			world.setBlockState(target, Rust.PART.getDefaultState().with(AssemblyBlock.CENTRAL, central));
			BlockUtil.getBlockEntity(world, target, AssemblyBlockEntity.class).orElseThrow().setAssembly(central ? instance : null, pair.offset(), pair.key());
		}

		return true;
	}

	public Identifier getIdentifier() {
		if (this.identifier == null) {
			this.identifier = RustRegistries.ASSEMBLY.getId(this);
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
		return constructor.create(rotation, origin);
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

	public Map<BlockPos, List<? extends ServerAssemblyDecal>> createDecalMap() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER ? null : ContainerUtil.remap(AssemblyDecalManager.getDecals(this), HashMap::new, list -> list.stream().map(ConfiguredDecal::create).toList());
	}

	public Map<BlockPos, List<? extends ServerAssemblyDecal>> getSharedDecalMap() {
		if (decals == null) {
			this.decals = createDecalMap();
		}

		return decals;
	}

}
