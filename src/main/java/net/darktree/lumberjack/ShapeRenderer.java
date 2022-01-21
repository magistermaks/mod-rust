package net.darktree.lumberjack;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class ShapeRenderer {

	public static void renderPrism(Matrix4f matrix, VertexConsumer buffer, int count, float height, float radius, float angle, float r, float g, float b, float a) {
		float step = (float) (Math.PI * 2) / count;

		float x = (float) (radius * Math.sin(angle));
		float z = (float) (radius * Math.cos(angle));

		while(count > 0) {
			count --;

			buffer.vertex(matrix, x, height, z).color(r, g, b, a).next();
			buffer.vertex(matrix, x, 0, z).color(r, g, b, a).next();

			angle += step;
			x = (float) (radius * Math.sin(angle));
			z = (float) (radius * Math.cos(angle));

			buffer.vertex(matrix, x, 0, z).color(r, g, b, a).next();
			buffer.vertex(matrix, x, height, z).color(r, b, b, a).next();
		}
	}

	public static void renderPrismAlong(MatrixStack matrices, VertexConsumer buffer, int count, float radius, float angle, float r, float g, float b, float a, float x1, float y1, float z1, float x2, float y2, float z2) {
		matrices.push();
		float length = RenderHelper.lookAlong(matrices, x1, y1, z1, x2, y2, z2);
		renderPrism(matrices.peek().getPositionMatrix(), buffer, count, length, radius, angle, r, g, b, a);
		matrices.pop();
	}

}
