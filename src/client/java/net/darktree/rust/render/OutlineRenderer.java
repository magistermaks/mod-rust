package net.darktree.rust.render;

import com.google.common.base.Suppliers;
import net.darktree.rust.RustClient;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class OutlineRenderer implements WorldRenderEvents.Last {

	private static final Vector3f UP = new Vector3f(0, 1, 0);
	private final MinecraftClient client;
	private Rotation rotation;
	private Supplier<BakedModel> supplier;

	public OutlineRenderer() {
		client = MinecraftClient.getInstance();
		rotation = Rotation.NORTH;
	}

	public void setBlockModel(Identifier model) {
		this.supplier = Suppliers.memoize(() -> client.getBakedModelManager().getModel(model));
	}

	public void onLast(WorldRenderContext context) {
		final HitResult result = client.crosshairTarget;

		while (RustClient.ROTATE_KEY.wasPressed()) {
			rotation = rotation.next();
		}

		if (result instanceof BlockHitResult hit && result.getType() == HitResult.Type.BLOCK) {
			final int offset = context.world().getBlockState(hit.getBlockPos()).isReplaceable() ? 0 : 1;
			final BlockPos pos = hit.getBlockPos().offset(hit.getSide(), offset);
			final BakedModel model = supplier.get();

			if (model != null) {
				final MatrixStack matrices = context.matrixStack();
				final Camera camera = context.camera();
				final RenderLayer layer = OutlineRenderLayer.getOutlineLayer(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
				final VertexConsumer buffer = context.consumers().getBuffer(layer);

				matrices.push();
				matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
				matrices.translate(pos.getX() + rotation.x, pos.getY(), pos.getZ() + rotation.z);
				matrices.multiply(RenderHelper.getDegreesQuaternion(UP, rotation.angle));

				RenderHelper.setTint(1, 1, 1, 0.8f);
				RenderHelper.renderModel(model, context.world(), pos, buffer, matrices, 0, 42);

				matrices.pop();
			}
		}
	}

}
