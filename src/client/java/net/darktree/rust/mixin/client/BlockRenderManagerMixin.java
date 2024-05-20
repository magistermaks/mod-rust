package net.darktree.rust.mixin.client;

import net.darktree.rust.Rust;
import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.block.AssemblyBlockEntity;
import net.darktree.rust.render.model.AssemblyModel;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * This mixin is used to render the block damage overlay over the whole assembly
 * no mater which of its blocks is being actually damaged by replacing the rendering
 * logic for our Assembly Part block (getting a custom model from AssemblyModel class)
 */
@Mixin(BlockRenderManager.class)
public abstract class BlockRenderManagerMixin {

	@Shadow @Final
	private BlockModelRenderer blockModelRenderer;

	@Shadow @Final
	private Random random;

	@Inject(
			method = "renderDamage",
			at = @At("HEAD"),
			cancellable = true
	)
	public void renderDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer consumer, CallbackInfo info) {
		if (state.getBlock() == Rust.PART /*&& !state.get(AssemblyBlock.CENTRAL)*/) {
			BlockEntity entity = world.getBlockEntity(pos);

			if (entity instanceof AssemblyBlockEntity assemblyEntity) {
				BlockPos center = assemblyEntity.getCenter();
				Optional<AssemblyInstance> optionalInstance = assemblyEntity.getAssembly();

				if (optionalInstance.isPresent()) {
					AssemblyInstance instance = optionalInstance.get();

					// matrix fixup
					matrices.translate(center.getX() - pos.getX(), center.getY() - pos.getY(), center.getZ() - pos.getZ());

					// this copies vanilla logic
					long seed = state.getRenderingSeed(pos);

					// get the whole model of the assembly
					BakedModel model = AssemblyModel.getModelFor(instance.getType(), instance.getRotation(), null);

					// render the damage over the whole model
					this.blockModelRenderer.render(world, model, state, pos, matrices, consumer, true, this.random, seed, OverlayTexture.DEFAULT_UV);
				}
			}

			info.cancel();
		}
	}

}
