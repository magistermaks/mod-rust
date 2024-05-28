package net.darktree.rust.mixin.client;

import com.google.gson.JsonObject;
import net.minecraft.client.render.model.json.ModelElement;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * This mixin is used to remove Minecraft's arbitrary
 * model size limitation of 3 by 2 by 3 blocks. Our
 * machines are to be bigger than that so this limit
 * needs to go. Fortunately no other issues occur after
 * the limit is raised.
 */
@Mixin(ModelElement.Deserializer.class)
public abstract class ModelElementMixin {

	@Inject(
			method = "deserializeTo",
			at = @At(
					value = "NEW",
					target = "com/google/gson/JsonParseException"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private void deserializeTo(JsonObject object, CallbackInfoReturnable<Vector3f> cir, Vector3f vector3f) {
		cir.setReturnValue(vector3f);
	}

	@Inject(
			method = "deserializeFrom",
			at = @At(
					value = "NEW",
					target = "com/google/gson/JsonParseException"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private void deserializeFrom(JsonObject object, CallbackInfoReturnable<Vector3f> cir, Vector3f vector3f) {
		cir.setReturnValue(vector3f);
	}

}
