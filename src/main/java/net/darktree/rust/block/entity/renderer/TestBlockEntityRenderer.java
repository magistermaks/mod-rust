package net.darktree.rust.block.entity.renderer;

import net.darktree.lumberjack.ModelHelper;
import net.darktree.lumberjack.ModelRenderer;
import net.darktree.lumberjack.ShapeRenderer;
import net.darktree.rust.block.entity.TestBlockEntity;
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

	public void drawAnimatedArcLine(MatrixStack matrices, VertexConsumer buffer, long tick, float delta, float upward, float x1, float y1, float z1, float x2, float y2, float z2) {
		upward *= (float) Math.sin((tick * 0.1f + delta * 0.1f) % (Math.PI / 2));

		drawArcLine(matrices, buffer, tick, 3, 0.009f, upward, x1, y1, z1, x2, y2, z2);
	}

	public void drawArcLine(MatrixStack matrices, VertexConsumer buffer, long seed, int depth, float separation, float upward, float x1, float y1, float z1, float x2, float y2, float z2) {
		Random random = new Random();

		int segments = 15;
		int sides = 4;
		float jaggedness = 0.3f;
		float radius = 0.005f;
		float r = 0.4f, g = 0.5f, b = 0.7f, a = 0.5f;

		for(int i = 0; i < depth; i ++) {
			random.setSeed(seed);
			drawJaggedLine(matrices, buffer, random, segments, sides, jaggedness, upward, radius, r, g, b, a, x1, y1, z1, x2, y2, z2);
			radius += separation;
		}
	}

	public void drawJaggedLine(MatrixStack matrices, VertexConsumer buffer, Random random, int segments, int count, float jaggedness, float upward, float radius, float r, float g, float b, float a, float x1, float y1, float z1, float x2, float y2, float z2) {
		float sx = (x2 - x1) / segments;
		float sy = (y2 - y1) / segments;
		float sz = (z2 - z1) / segments;

		float fx = x1, fy = y1, fz = z1;
		float tx, ty, tz;

		float step = (float) Math.PI / segments;

		for(int i = 1; i < segments; i ++) {
			float offset = (float) Math.sin(step * i) * upward;

			tx = x1 + sx * i + (random.nextFloat() - 0.5f) * jaggedness;
			ty = y1 + sy * i + (random.nextFloat() - 0.5f) * jaggedness + offset;
			tz = z1 + sz * i + (random.nextFloat() - 0.5f) * jaggedness;

			ShapeRenderer.renderPrismAlong(matrices, buffer, count, radius, 0, r, g, b, a, fx, fy, fz, tx, ty, tz);

			fx = tx;
			fy = ty;
			fz = tz;
		}

		ShapeRenderer.renderPrismAlong(matrices, buffer, count, radius, 0, r, g, b, a, fx, fy, fz, x2, y2, z2);
	}

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

//		ShapeRenderer.renderPrismAlong(matrices, buffer, 6, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, x, y, 3);

		drawAnimatedArcLine(matrices, buffer, entity.getWorld().getTime() + 32, tickDelta, 0.65f, 0, 0, 0, x, y, 3);
		drawAnimatedArcLine(matrices, buffer, entity.getWorld().getTime() + 71, tickDelta, 0.65f, 0, 0, 0, 0, 0, 3);

//		drawJaggedLine(matrices, buffer, new Random(entity.getWorld().getTime()), 15, 4, 0.3f, 0.01f, 0.4f, 0.5f, 0.7f, 0.5f, 0, 0, 0, x, y, 3);
//
//		drawJaggedLine(matrices, buffer, new Random(entity.getWorld().getTime()), 15, 4, 0.3f, 0.01f, 0.4f, 0.5f, 0.7f, 0.5f, 0, 0, 0, 0, 0, 3);

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

			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, 0, 0, -3);
			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, 3, 0, -3);
			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, 0, 3, -3);
			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, -3, 0, -3);
			ShapeRenderer.renderPrismAlong(matrices, buffer, 5, r, 0, cr, cg, cb, 0.35f, 0, 0, 0, 0, -3, -3);
//				}
//			}
		}



	}
}
