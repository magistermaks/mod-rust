package net.darktree.rust;

import net.darktree.lumberjack.ModelHelper;
import net.darktree.rust.block.TestBlock;
import net.darktree.rust.block.entity.TestBlockEntity;
import net.darktree.rust.block.entity.renderer.TestBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Initializer implements ModInitializer, ClientModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Rust");

	private static Identifier id(String name) {
		return new Identifier("rust", name);
	}

	public static final Block TEST;
	public static final BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY;

	static {
		TEST = Registry.register(Registry.BLOCK, id("test"), new TestBlock(FabricBlockSettings.of(Material.METAL).strength(10)));
		TEST_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("test_block_entity"), FabricBlockEntityTypeBuilder.create(TestBlockEntity::new, TEST).build(null));
	}

	@Override
	public void onInitialize() {

	}

	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(TEST_BLOCK_ENTITY, TestBlockEntityRenderer::new);

		ModelHelper.loadModels(id("example"));

	}

}
