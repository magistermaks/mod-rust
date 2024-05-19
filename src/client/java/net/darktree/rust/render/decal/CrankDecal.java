package net.darktree.rust.render.decal;

import net.darktree.rust.Rust;
import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.assembly.AssemblyRenderView;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.darktree.rust.block.entity.ServerAssemblyDecal;
import net.darktree.rust.render.RenderHelper;
import net.darktree.rust.render.model.RustModels;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class CrankDecal implements ServerAssemblyDecal, ClientAssemblyDecal {

	protected float rotationToAngle(BlockRotation r) {
		if (r == BlockRotation.CLOCKWISE_90) return 90;
		if (r == BlockRotation.CLOCKWISE_180) return 180;
		if (r == BlockRotation.COUNTERCLOCKWISE_90) return 270;
		return 0;
	}

	@Override
	public void tick(World world, AssemblyBlockEntity entity, AssemblyInstance instance) {
	}

	@Override
	public void fetch(AssemblyInstance instance) {
	}

	@Override
	public void render(World world, BlockPos pos, AssemblyRenderView instance, float delta, MatrixStack matrices, VertexConsumer consumer, int overlay, float r, float g, float b, float a) {
		matrices.push();

		matrices.translate(0.5, 0.5, 0.5); // + absolute_offset
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(360 - rotationToAngle(instance.getRotation()))); // + apply_block_rotation
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) instance.getDecalPushConstant(Rust.CRANK).getLinear(delta)));
		matrices.translate(-0.5, -0.5, -0.5 + 2/16f); // + relative_offset

		RenderHelper.setTint(r, g, b, a); // + tint

		BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(RustModels.CRANK);
		RenderHelper.renderModel(model, world, pos, consumer, matrices, overlay, 42);

		matrices.pop();

//		BlockRotation rotation = blockEntity.getRotation();
//		matrices.push();
//		matrices.translate(0.5, 2.5, 0.5);
//		//matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
//		matrices.scale(0.02f, -0.02f, 0.02f);
//		text.draw("f=" + (int) blockEntity.velocity, 0.1f, 0.1f, 0xffffffff, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
//
//		matrices.pop();
//
//		matrices.translate(0.5, 0.5, 0.5); // + absolute_offset
////
////		// Rotate the item
////		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((blockEntity.getWorld().getTime() + tickDelta) * 4));
//		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(360 - mapToDeg(rotation))); // + apply_block_rotation
//		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) blockEntity.angle ));
////
//
//		matrices.translate(-0.5, -0.5, -0.5 + 2/16f); // + relative_offset
//
////		MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.JUKEBOX), ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, blockEntity.getWorld(), 0);
//
//		final RenderLayer layer = RenderLayer.getSolid();
//		final VertexConsumer buffer = vertexConsumers.getBuffer(layer);
//
//		RenderHelper.setTint(1, 1, 1, 1); // + tint
//
//		BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(RustModels.CRANK);
////		RenderHelper.renderModel(model, blockEntity.getWorld(), blockEntity.getPos(), buffer, matrices, overlay, 42);
//
//		manager.getModelRenderer().render(matrices.peek(), buffer, null, model, 1.0f, 1.0f, 1.0f, light, overlay);
	}

}
