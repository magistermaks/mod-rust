package net.darktree.rust.mixin.client;

import net.darktree.rust.render.model.AssemblyModel;
import net.darktree.rust.render.model.RustModels;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

	@Inject(
			method = "bake",
			at = @At("TAIL")
	)
	public void bake(BiFunction<Identifier, SpriteIdentifier, Sprite> loader, CallbackInfo info) {
		AssemblyModel.bakeUnderlyingModels(((ModelLoader) (Object) this).new BakerImpl(loader, RustModels.BAKER_DUMMY_ID), loader);
	}

}
