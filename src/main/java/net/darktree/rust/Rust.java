package net.darktree.rust;

import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.assembly.AssemblyType;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.AssemblyBlockEntity;
import net.darktree.rust.assembly.decal.DecalPushConstant;
import net.darktree.rust.item.AssemblyItem;
import net.darktree.rust.item.DebugItem;
import net.darktree.rust.network.RustPackets;
import net.darktree.rust.util.VoxelUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class Rust implements ModInitializer {

	// basic setup
	public static final String NAMESPACE = "rust";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Item.Settings ITEM_SETTINGS = new Item.Settings();

	// sounds
	public static final Identifier ROTATE_SOUND_ID = Rust.id("rotate");
	public static SoundEvent ROTATE_SOUND_EVENT = SoundEvent.of(ROTATE_SOUND_ID);

	// helper for making Identifiers
	public static Identifier id(String name) {
		return new Identifier(NAMESPACE, name);
	}

	public static final DecalPushConstant.Type CRANK = DecalPushConstant.Type.of(0);
	public static final DecalPushConstant.Type SPEED = DecalPushConstant.Type.of(0);

	public static final AssemblyType ASSEMBLY = new AssemblyType(
			Set.of(CRANK, SPEED),
			List.of(new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 0, -1)),
			VoxelUtil.begin().addCuboid(0, 0, 0, 16, 16, 16).addCuboid(2, 2, -13, 14, 14, 0).addCuboid(0, 16, 0, 16, 32, 16).build(),
			BlockSoundGroup.ANVIL,
			AssemblyInstance::new,
			(tooltips, context) -> {}
	);

	public static final DebugItem DEBUG_ITEM = new DebugItem(ITEM_SETTINGS);
	public static final AssemblyBlock PART = new AssemblyBlock(AbstractBlock.Settings.create().allowsSpawning(Blocks::never).ticksRandomly().strength(0.5f, 0.5f).mapColor(DyeColor.LIGHT_GRAY).pistonBehavior(PistonBehavior.BLOCK).solid());
	public static final AssemblyItem TEST_ITEM = new AssemblyItem(ITEM_SETTINGS, ASSEMBLY);
	public static final BlockEntityType<AssemblyBlockEntity> ASSEMBLY_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(AssemblyBlockEntity::new, PART).build();

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, id("test"), PART);
		Registry.register(Registries.ITEM, id("test"), TEST_ITEM);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, id("assembly"), ASSEMBLY_BLOCK_ENTITY);
		Registry.register(Registries.SOUND_EVENT, ROTATE_SOUND_ID, ROTATE_SOUND_EVENT);
		Registry.register(Registries.ITEM, id("debug"), DEBUG_ITEM);

		Registry.register(RustRegistries.ASSEMBLY, id("test"), ASSEMBLY);
		Registry.register(RustRegistries.CONSTANT, id("crank"), CRANK);
		Registry.register(RustRegistries.CONSTANT, id("speed"), SPEED);

		RustPackets.init();
	}

}