package net.darktree.rust;

import net.darktree.rust.assembly.BlockAssembly;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.darktree.rust.item.AssemblyItem;
import net.darktree.rust.network.RustNetworking;
import net.darktree.rust.util.VoxelUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Rust implements ModInitializer {

	public static final String NAMESPACE = "rust";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Item.Settings ITEM_SETTINGS = new Item.Settings();

	public static final RegistryKey<Registry<BlockAssembly>> ASSEMBLY_REGISTRY_KEY = RegistryKey.ofRegistry(Rust.id("assembly"));
	public static final Registry<BlockAssembly> ASSEMBLY_REGISTRY = FabricRegistryBuilder.createSimple(ASSEMBLY_REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static Identifier id(String name) {
		return new Identifier(NAMESPACE, name);
	}

	public static final BlockAssembly ASSEMBLY = new BlockAssembly(
			List.of(new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 0, -1)),
			VoxelUtil.begin().addCuboid(0, 0, 0, 16, 16, 16).addCuboid(2, 2, -13, 14, 14, 0).addCuboid(0, 16, 0, 16, 32, 16).build(),
			BlockSoundGroup.ANVIL
	);

	public static final AssemblyBlock TEST = new AssemblyBlock(AbstractBlock.Settings.create().dropsNothing().solid());
	public static final AssemblyItem TEST_ITEM = new AssemblyItem(ITEM_SETTINGS, ASSEMBLY);
	public static final BlockEntityType<AssemblyBlockEntity> ASSEMBLY_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(AssemblyBlockEntity::new, TEST).build();

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, id("test"), TEST);
		Registry.register(Registries.ITEM, id("test"), TEST_ITEM);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, id("assembly"), ASSEMBLY_BLOCK_ENTITY);
		Registry.register(ASSEMBLY_REGISTRY, id("test"), ASSEMBLY);

		RustNetworking.init();
	}

}