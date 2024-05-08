package net.darktree.rust.mixin.client;

import net.darktree.rust.Rust;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is used to offset the breaking target so that it aligns with
 * the assembly center (the block with property CENTER set to TRUE) as only that
 * block has any model, this makes it so trying to break any block in the assembly will
 * (for the client) look as if the player was braking the central block
 */
@Mixin(BlockRenderManager.class)
public abstract class BlockRenderManagerMixin {

	@Shadow
	public abstract void renderDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer);

	@Unique
	private boolean recursed = false;

	@Inject(
			method = "renderDamage",
			at = @At("HEAD"),
			cancellable = true
	)
	public void renderDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer consumer, CallbackInfo info) {
		if (state.getBlock() == Rust.PART && !state.get(AssemblyBlock.CENTRAL)) {
			BlockEntity entity = world.getBlockEntity(pos);

			if (entity instanceof AssemblyBlockEntity assemblyEntity) {
				BlockPos center = assemblyEntity.getCenter();

				// make sure we don't fall into infinite recursion
				// if another mod does something here
				if (recursed) {
					Rust.LOGGER.warn("RenderDamage recursed too deep! Is another mod interfering?");
					info.cancel();
				}

				// matrix fixup
				matrices.translate(center.getX() - pos.getX(), center.getY() - pos.getY(), center.getZ() - pos.getZ());

				// recursive call but now targeting a different block
				recursed = true; renderDamage(world.getBlockState(center), center, world, matrices, consumer); recursed = false;
				info.cancel();
			}
		}
	}

}
