package net.darktree.rust;

import net.darktree.rust.block.entity.AssemblyDecalManager;
import net.darktree.rust.block.entity.DecalType;
import net.darktree.rust.render.OutlineRenderer;
import net.darktree.rust.render.decal.AssemblyBlockEntityRenderer;
import net.darktree.rust.render.decal.CrankDecal;
import net.darktree.rust.render.model.RustModels;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.lwjgl.glfw.GLFW;

public class RustClient implements ClientModInitializer {

	public static OutlineRenderer OUTLINER = new OutlineRenderer();
	public static KeyBinding ROTATE_KEY = new KeyBinding("key.rust.rotate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.MISC_CATEGORY);

	DecalType<CrankDecal> CRANK_DECAL = new DecalType<>(CrankDecal::new);

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(ROTATE_KEY);
		WorldRenderEvents.AFTER_ENTITIES.register(OUTLINER);

		BlockEntityRendererFactories.register(Rust.ASSEMBLY_BLOCK_ENTITY, AssemblyBlockEntityRenderer::new);
		AssemblyDecalManager.register(Rust.ASSEMBLY, CRANK_DECAL.at(0, 0, -1));

		Registry.register(RustRegistries.DECAL, Rust.id("crank"), CRANK_DECAL);

		RustModels.init();
	}

}