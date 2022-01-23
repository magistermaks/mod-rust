package net.darktree.rust.block.entity.renderer;

import net.darktree.lumberjack.ModelHelper;
import net.darktree.lumberjack.ModelRenderer;
import net.darktree.rust.block.entity.TestBlockEntity;
import net.darktree.rust.render.ArcRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TestBlockEntityRenderer implements BlockEntityRenderer<TestBlockEntity> {

	public TestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

	}

	Identifier id = new Identifier("rust", "example");
	BakedModel model = ModelHelper.getModel(id);

	@Override
	public void render(TestBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.translate(0.5, 1.25, 0.5);
		ModelRenderer.renderModel(model, entity.getWorld(), entity.getPos(), vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()), matrices, overlay, 42);
		matrices.pop();

		VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLightning());

		float tt = (entity.getWorld().getLevelProperties().getTime() + tickDelta) / 10;

		float x = (float) (3 * Math.sin(tt));
		float y = (float) (3 * Math.cos(tt));

		long time = entity.getWorld().getTime();

		ArcRenderer.renderNoise(matrices, buffer, time + 7, 24, 0.3f, 0, 0, 3);

		ArcRenderer.renderArc(matrices, buffer, time + 90, tickDelta, 0, 6, 0, x, 6, y);
		ArcRenderer.renderArc(matrices, buffer, time + 32, tickDelta, 0, 6, 0, -x, 6, -y);
		ArcRenderer.renderArc(matrices, buffer, time + 71, tickDelta, 0, 0, 0, 0, 0, 3);
		ArcRenderer.renderArc(matrices, buffer, time + 71, tickDelta, 0, 0, 0, 0, 0, -1);

	}
}
