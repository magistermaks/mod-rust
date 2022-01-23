package net.darktree.rust.block.entity.renderer;

import net.darktree.lumberjack.ModelHelper;
import net.darktree.lumberjack.ModelRenderer;
import net.darktree.lumberjack.ShapeRenderer;
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

import java.util.Random;

public class TestBlockEntityRenderer implements BlockEntityRenderer<TestBlockEntity> {

	public TestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

	}

	Identifier id = new Identifier("rust", "example");
	BakedModel model = ModelHelper.getModel(id);

	ShapeRenderer.Segment[] segments = new ShapeRenderer.Segment[64];
	long timec = -1;

	@Override
	public void render(TestBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.translate(0.5, 1.25, 0.5);
		ModelRenderer.renderModel(model, entity.getWorld(), entity.getPos(), vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()), matrices, overlay, 42);
		matrices.pop();

		VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLightning());


		float tt = (entity.getWorld().getLevelProperties().getTime() + tickDelta) / 10;
//
		float x = (float) (3 * Math.sin(tt));
		float y = (float) (3 * Math.cos(tt));

Random random = new Random(42);

		long time = entity.getWorld().getTime();

		ArcRenderer.renderNoise(matrices, buffer, time + 7, 8, 0.4f, 0, 0, 3);

//		ShapeRenderer.renderPrismAlong(matrices, buffer, 6, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, x, y, 3);

		ArcRenderer.renderArc(matrices, buffer, time + 90, tickDelta, 0, 6, 0, x, 6, y);
		ArcRenderer.renderArc(matrices, buffer, time + 32, tickDelta, 0, 6, 0, -x, 6, -y);

		ArcRenderer.renderArc(matrices, buffer, time + 71, tickDelta, 0, 0, 0, 0, 0, 3);

		if(time != this.timec) {
			this.timec = time;
			float jaggedness = 0.1f;
			random.setSeed(time);

			for(int i = 0; i < 64; i += 1) {
				this.segments[i] = new ShapeRenderer.Segment((random.nextFloat() - 0.5f) * jaggedness, (random.nextFloat() - 0.5f) * jaggedness);
			}
		}

		ShapeRenderer.renderPrismAlong(matrices, buffer, 4, 0.01f, 0, 0.1f, 0.9f, 0.1f, 0.6f, 0, 0, 0, 0, 64, 0, this.segments);
		ShapeRenderer.renderPrismAlong(matrices, buffer, 4, 0.02f, 0, 0.1f, 0.9f, 0.1f, 0.6f, 0, 0, 0, 0, 64, 0, this.segments);
		ShapeRenderer.renderPrismAlong(matrices, buffer, 4, 0.04f, 0, 0.1f, 0.9f, 0.1f, 0.6f, 0, 0, 0, 0, 64, 0, this.segments);

//		drawJaggedLine(matrices, buffer, new Random(entity.getWorld().getTime()), 15, 4, 0.3f, 0.01f, 0.4f, 0.5f, 0.7f, 0.5f, 0, 0, 0, x, y, 3);
//
//		drawJaggedLine(matrices, buffer, new Random(entity.getWorld().getTime()), 15, 4, 0.3f, 0.01f, 0.4f, 0.5f, 0.7f, 0.5f, 0, 0, 0, 0, 0, 3);

		random.setSeed(52);

		float[] fs = new float[8];
		float[] gs = new float[8];
		float h = 0.0F;
		float j = 0.0F;


		for(int k = 7; k >= 0; --k) {
			fs[k] = h;
			gs[k] = j;
			h += (float)(random.nextInt(11) - 5);
			j += (float)(random.nextInt(11) - 5);
		}




		for(int l = 0; l < 4; ++l) {
			Random random2 = new Random(42);

			int m = 0;
			//for(int m = 0; m < 3; ++m) {
				int n = 7;
				int o = 0;
				if (m > 0) {
					n = 7 - m;
				}

				if (m > 0) {
					o = n - 2;
				}

				float p = fs[n] - h;
				float q = gs[n] - j;

//				for(int r = n; r >= o; --r) {
					float s = p;
					float t = q;
					if (m == 0) {
						p += (float)(random2.nextInt(11) - 5);
						q += (float)(random2.nextInt(11) - 5);
					} else {
						p += (float)(random2.nextInt(31) - 15);
						q += (float)(random2.nextInt(31) - 15);
					}

					float cb = 0.5F;
					float cg = 0.45F;
					float cr = 0.7F;

					float r = 0.04F + (float)l * 0.08F;

			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, 0, 0, -3, ShapeRenderer.NO_OFFSET);
			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, 3, 0, -3, ShapeRenderer.NO_OFFSET);
			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, 0, 3, -3, ShapeRenderer.NO_OFFSET);
			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, -3, 0, -3, ShapeRenderer.NO_OFFSET);
			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, 0, -3, -3, ShapeRenderer.NO_OFFSET);
//				}
//			}
		}



	}
}
