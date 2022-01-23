package net.darktree.rust.render;

import net.darktree.lumberjack.RenderHelper;
import net.darktree.lumberjack.ShapeRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class ArcRenderer {

	private static final Random RANDOM = new Random();

	/** upwards trend, emulating the force of convection moving ions up */
	private static final float upward = 0.65f;

	/** distance between the visual layers */
	private static final float separation = 0.005f;

	/**
	 * Render an arc between two points.
	 */
	public static void renderArc(MatrixStack matrices, VertexConsumer buffer, long tick, float delta, float x1, float y1, float z1, float x2, float y2, float z2) {
		final float r = 0.4f, g = 0.5f, b = 0.75f, t = delta * 0.1f;

		float u = upward * MathHelper.sin(((tick + 1) * 0.1f + t) % MathHelper.HALF_PI);
		renderLayeredArcLine(matrices, buffer, tick + 1, separation, u, x1, y1, z1, x2, y2, z2, r, g, b, 0.5f, 20);

		if(RenderHelper.shouldRenderDetails()) {
			u = upward * MathHelper.sin((tick + t) % MathHelper.HALF_PI);
			renderLayeredArcLine(matrices, buffer, tick + 2, separation, u, x1, y1, z1, x2, y2, z2, r, g, b, 0.3f, 20);

			u = upward * MathHelper.sin((tick + 3 + t) % MathHelper.HALF_PI);
			renderLayeredArcLine(matrices, buffer, tick + 3, separation, u, x1, y1, z1, x2, y2, z2, r, g, b, 0.1f, 20);
		}
	}

	/**
	 * Render a tesla-coil-like arc source at the given point
	 */
	public static void renderNoise(MatrixStack matrices, VertexConsumer buffer, long tick, int count, float radius, float x, float y, float z) {
		final float r = 0.4f, g = 0.5f, b = 0.75f;
//		RANDOM.setSeed(tick * 123);

		float[] sizes = {0.1f, 0.3f, 0.5f};

		// make the random values not continues
		RANDOM.setSeed(tick);
		Random random = new Random(RANDOM.nextLong());

		for(int i = 0; i < count; i++) {
			double tx = random.nextGaussian();
			double ty = random.nextGaussian();
			double tz = random.nextGaussian();

			double scale = 1.0 / Math.sqrt(x*x + y*y + z*z);
			double length = radius * (random.nextFloat() + 1);

			tx = x + tx * scale * length;
			ty = x + ty * scale * length;
			tz = z + tz * scale * length;

			renderLayeredArcLine(matrices, buffer, tick + i, separation, 0, x, y, z, (float) tx, (float) ty, (float) tz, r * 1.34f, g, b, sizes[i % 3], 4);
		}
	}

	/**
	 * Render a simple single layered jagged arc-like line between two points.
	 * For rendering detailed arcs use the {@link ArcRenderer#renderArc} method.
	 */
	public static void renderLayeredArcLine(MatrixStack matrices, VertexConsumer buffer, long seed, float separation, float upward, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a, int segments) {
		/// TODO: 'segments' probably should be based on length

		float radius = 0.002f;

		for(int i = 0; i < 3; i ++) {
			RANDOM.setSeed(seed);
			renderArcLine(matrices, buffer, RANDOM, segments, 4, 0.2f, upward, radius, true, r, g, b, a, x1, y1, z1, x2, y2, z2);
			radius += separation;
		}
	}

	/**
	 * Render a simple single jagged arc-like line between two points.
	 * For rendering detailed arcs use the {@link ArcRenderer#renderArc} method.
	 */
	public static void renderArcLine(MatrixStack matrices, VertexConsumer buffer, Random random, int segments, int count, float jaggedness, float upward, float radius, boolean noise, float r, float g, float b, float a, float x1, float y1, float z1, float x2, float y2, float z2) {
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

			ShapeRenderer.renderPrismAlong(matrices, buffer, count, radius, 0, r, g, b, a, fx, fy, fz, tx, ty, tz, ShapeRenderer.NO_OFFSET);

			// split into sub-arcs
			if(noise && i != 1 && random.nextInt(20) == 0) {
				int j = random.nextBoolean() ? 3 : -3;

				float ttx = tx + sx * j + (random.nextFloat() - 0.5f) * jaggedness * 3;
				float tty = ty + sy * j + (random.nextFloat() - 0.5f) * jaggedness * 5;
				float ttz = tz + sz * j + (random.nextFloat() - 0.5f) * jaggedness * 3;

				renderArcLine(matrices, buffer, random, 3, count, jaggedness * 0.8f, 0, radius * 0.6f, true, r * 1.34f, g, b, a * 0.6f, fx, fy, fz, ttx, tty, ttz);
			}

			fx = tx;
			fy = ty;
			fz = tz;
		}

		ShapeRenderer.renderPrismAlong(matrices, buffer, count, radius, 0, r, g, b, a, fx, fy, fz, x2, y2, z2, ShapeRenderer.NO_OFFSET);
	}

}
