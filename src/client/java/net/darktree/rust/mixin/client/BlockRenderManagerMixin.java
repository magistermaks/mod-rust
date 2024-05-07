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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderManager.class)
public abstract class BlockRenderManagerMixin {

	@Shadow
	public abstract void renderDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer);

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

				// matrix fixup
				matrices.translate(center.getX() - pos.getX(), center.getY() - pos.getY(), center.getZ() - pos.getZ());

				renderDamage(world.getBlockState(center), center, world, matrices, consumer);
				info.cancel();
			}
		}
	}

}
