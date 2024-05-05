package net.darktree.rust;

import net.darktree.rust.item.AssemblyItem;
import net.darktree.rust.network.RustNetworking;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Rust implements ModInitializer {

	public static final String NAMESPACE = "rust";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Item.Settings ITEM_SETTINGS = new Item.Settings();

	public static Identifier id(String name) {
		return new Identifier(NAMESPACE, name);
	}

	public static final BlockAssembly ASSEMBLY = new BlockAssembly(
			List.of(new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 0, -1)),
			VoxelShapes.combine(Block.createCuboidShape(0, 0, 0, 16, 16, 16), Block.createCuboidShape(0, 0, -16, 16, 16, 0), BooleanBiFunction.OR)
	);

	public static final Block TEST = new Block(AbstractBlock.Settings.create().dropsNothing().solid());
	public static final AssemblyItem TESTI = new AssemblyItem(ITEM_SETTINGS, ASSEMBLY);

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, id("test"), TEST);
		Registry.register(Registries.ITEM, id("test"), TESTI);

		RustNetworking.init();
	}

}