package net.darktree.lumberjack;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockRenderView;

import java.util.List;

public class RenderHelper {

	public static void renderFlatQuad(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, BakedQuad quad, float brightness, int light, int overlay) {
		vertexConsumer.quad(matrix, quad, new float[]{brightness, brightness, brightness, brightness}, 1.0f, 1.0f, 1.0f, new int[]{light, light, light, light}, overlay, true);
	}

	public static void renderFlatQuads(BlockRenderView world, BlockPos pos, int light, int overlay, boolean useWorldLight, MatrixStack matrices, VertexConsumer vertexConsumer, List<BakedQuad> quads) {
		for (BakedQuad bakedQuad : quads) {
			if (useWorldLight) {
				BlockPos source = isQuadExtended(bakedQuad.getVertexData(), bakedQuad.getFace()) ? pos.offset(bakedQuad.getFace()) : pos;
				light = WorldRenderer.getLightmapCoordinates(world, source.up());
			}

			float brightness = world.getBrightness(bakedQuad.getFace(), bakedQuad.hasShade());
			renderFlatQuad(vertexConsumer, matrices.peek(), bakedQuad, brightness, light, overlay);
		}
	}

	public static boolean shouldRenderDetails() {
		return MinecraftClient.getInstance().options.graphicsMode != GraphicsMode.FAST;
	}

	/**
	 * Scales and transforms the matrices to point at (x2, y2, z2) from (x1, y1, z1)
	 * and returns the distance between those points.
	 *
	 * based on: stackoverflow.com/q/14337441
	 */
	public static float lookAlong(MatrixStack matrices, float x1, float y1, float z1, float x2, float y2, float z2) {
		x2 -= x1; y2 -= y1; z2 -= z1;

		matrices.translate(-x1, y1, -z1);

		// if straight down or up
		if(x2 == 0 && z2 == 0) {
			if(y2 > y1) {
				return y2 - y1;
			}else{
				matrices.multiply( Quaternion.fromEulerXyz((float) Math.PI, 0, 0));
				return y1 - y2;
			}
		}

		// we need the distance anyway so fastInverseSqrt won't be helpful
		float distance = (float) Math.sqrt(x2*x2 + y2*y2 + z2*z2);
		float scale = 1.0f / distance;

		x2 *= scale;
		y2 *= scale;
		z2 *= scale;

		final Vec3f axis = new Vec3f(-z2, 0, x2);
		axis.normalize();
		Quaternion q = new Quaternion(axis, (float) Math.acos(y2), false);
		matrices.multiply(q);

		return distance;
	}

	private static boolean isQuadExtended(int[] vertexData, Direction face) {
		float f =  32.0f, g =  32.0f, h =  32.0f;
		float i = -32.0F, j = -32.0F, k = -32.0F;

		for(int l = 0; l < 32; l += 8) {
			float m = Float.intBitsToFloat(vertexData[l]);
			float n = Float.intBitsToFloat(vertexData[l + 1]);
			float o = Float.intBitsToFloat(vertexData[l + 2]);
			f = Math.min(f, m);
			g = Math.min(g, n);
			h = Math.min(h, o);
			i = Math.max(i, m);
			j = Math.max(j, n);
			k = Math.max(k, o);
		}

		return switch (face) {
			case DOWN -> g == j && g < 1.0E-4F;
			case UP -> g == j && j > 0.9999F;
			case NORTH -> h == k && h < 1.0E-4F;
			case SOUTH -> h == k && k > 0.9999F;
			case WEST -> f == i && f < 1.0E-4F;
			case EAST -> f == i && i > 0.9999F;
		};
	}

}
