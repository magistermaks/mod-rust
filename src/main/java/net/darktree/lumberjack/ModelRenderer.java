package net.darktree.lumberjack;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Random;

public class ModelRenderer {

	private static final Random RANDOM = new Random();

	public static void renderModel(BakedModel model, BlockRenderView view, BlockPos pos, VertexConsumer vertexConsumer, MatrixStack matrices, int overlay, int seed) {
		BlockPos.Mutable mutable = pos.mutableCopy();

		for (Direction direction : Direction.values()) {
			RANDOM.setSeed(seed);
			List<BakedQuad> list = model.getQuads(null, direction, RANDOM);

			if (!list.isEmpty()) {
				mutable.set(pos, direction);
				int j = WorldRenderer.getLightmapCoordinates(view, mutable);
				RenderHelper.renderFlatQuads(view, pos, j, overlay, false, matrices, vertexConsumer, list);
			}
		}

		RANDOM.setSeed(seed);
		List<BakedQuad> list2 = model.getQuads(null, null, RANDOM);

		if (!list2.isEmpty()) {
			RenderHelper.renderFlatQuads(view, pos, -1, overlay, true, matrices, vertexConsumer, list2);
		}
	}

}
