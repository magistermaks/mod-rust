package net.darktree.rust;

import net.darktree.rust.assembly.decal.DecalType;
import net.darktree.rust.json.JsonAssemblyLoader;
import net.darktree.rust.render.OutlineRenderer;
import net.darktree.rust.render.block.AssemblyBlockEntityRenderer;
import net.darktree.rust.render.decal.RevolvingDecal;
import net.darktree.rust.render.model.RustModels;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import org.lwjgl.glfw.GLFW;

public class RustClient implements ClientModInitializer {

	public static OutlineRenderer OUTLINER = new OutlineRenderer();
	public static KeyBinding ROTATE_KEY = new KeyBinding("key.rust.rotate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.MISC_CATEGORY);

	public static final DecalType<RevolvingDecal, RevolvingDecal.Config> REVOLVING = new DecalType<>(RevolvingDecal::new, RevolvingDecal.Config::new);

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(ROTATE_KEY);
		WorldRenderEvents.AFTER_ENTITIES.register(OUTLINER);

		BlockEntityRendererFactories.register(Rust.ASSEMBLY_BLOCK_ENTITY, AssemblyBlockEntityRenderer::new);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new JsonAssemblyLoader());
		Registry.register(RustRegistries.DECAL, Rust.id("revolving"), REVOLVING);

		RustModels.init();
	}

}