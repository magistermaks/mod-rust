package net.darktree.rust.mixin.client;

import com.mojang.blaze3d.systems.VertexSorter;
import net.darktree.rust.render.OutlineRenderLayer;
import net.darktree.rust.render.RenderHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLayer.class)
public abstract class RenderLayerMixin extends RenderPhase {

	public RenderLayerMixin(String name, Runnable beginAction, Runnable endAction) {
		super(name, beginAction, endAction);
	}

	@Inject(
			method = "draw",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/BufferBuilder;setSorter(Lcom/mojang/blaze3d/systems/VertexSorter;)V",
					shift = At.Shift.AFTER
			)
	)
	public void draw(BufferBuilder buffer, VertexSorter sorter, CallbackInfo ci) {
		if (OutlineRenderLayer.isOutlineLayer(name)) {
			buffer.setSorter(OutlineRenderLayer.getInvertedVertexSorter());
		}
	}

}
