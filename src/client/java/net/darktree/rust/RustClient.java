package net.darktree.rust;

import net.darktree.rust.render.OutlineRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class RustClient implements ClientModInitializer {

	public static OutlineRenderer OUTLINER = new OutlineRenderer();
	public static KeyBinding ROTATE_KEY = new KeyBinding("key.rust.rotate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.MISC_CATEGORY);

	@Override
	public void onInitializeClient() {

		Identifier myModel = Rust.id("block/test");

		ModelLoadingPlugin.register(context -> {
			context.addModels(myModel);
		});

		VoxelShape shape = VoxelShapes.combine(Block.createCuboidShape(0, 0, 0, 16, 16, 16), Block.createCuboidShape(0, 0, -16, 16, 16, 0), BooleanBiFunction.OR);
		BlockAssembly assembly = new BlockAssembly(List.of(new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 0, -1)), shape);
		OUTLINER.setBlockAssembly(assembly);

		KeyBindingHelper.registerKeyBinding(ROTATE_KEY);
		WorldRenderEvents.AFTER_ENTITIES.register(OUTLINER);

		OUTLINER.setBlockModel(myModel);
	}

}