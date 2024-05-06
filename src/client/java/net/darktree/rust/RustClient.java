package net.darktree.rust;

import net.darktree.rust.render.OutlineRenderer;
import net.darktree.rust.render.model.RustModels;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class RustClient implements ClientModInitializer {

	public static OutlineRenderer OUTLINER = new OutlineRenderer();
	public static KeyBinding ROTATE_KEY = new KeyBinding("key.rust.rotate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.MISC_CATEGORY);

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(ROTATE_KEY);
		WorldRenderEvents.AFTER_ENTITIES.register(OUTLINER);
		OUTLINER.setBlockModel(RustModels.TEST);

		RustModels.init();
	}

}