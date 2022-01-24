package net.darktree.lumberjack.mixin;

import net.darktree.lumberjack.particle.ParticleHelper;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

	@Inject(method="loadTextureList", at=@At("HEAD"), cancellable=true)
	public void loadTextureList(ResourceManager resourceManager, Identifier id, Map<Identifier, List<Identifier>> result, CallbackInfo ci) {
		List<Identifier> sprites = ParticleHelper.particleSprites.get(id);

		if(sprites != null) {
			result.put(id, sprites);

			// don't hoard the RAM
			ParticleHelper.particleSprites.remove(id);

			ci.cancel();
		}
	}

}
