package net.darktree.rust.render;

import com.google.common.base.Suppliers;
import net.darktree.rust.assembly.AssemblyType;
import net.darktree.rust.RustClient;
import net.darktree.rust.item.AssemblyItem;
import net.darktree.rust.network.AssemblyRotationC2SPacket;
import net.darktree.rust.network.RustPackets;
import net.darktree.rust.util.duck.PlayerRotationView;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class OutlineRenderer implements WorldRenderEvents.AfterEntities {

	private static final Vector3f UP = new Vector3f(0, 1, 0);
	private final MinecraftClient client;
	private Rotation rotation;
	private Supplier<BakedModel> supplier;

	private float r, g, b;
	private boolean hadLast = false;

	public OutlineRenderer() {
		client = MinecraftClient.getInstance();
		rotation = Rotation.NORTH;
	}

	public void setBlockModel(Identifier model) {
		this.supplier = Suppliers.memoize(() -> client.getBakedModelManager().getModel(model));
	}

	public void afterEntities(WorldRenderContext context) {

		if (client.player == null) {
			return;
		}

		final HitResult result = client.crosshairTarget;
		final Item item = client.player.getStackInHand(client.player.getActiveHand()).getItem();
		boolean rotationUpdate = false;

		if (result.getType() == HitResult.Type.MISS) {
			hadLast = false;
			return;
		}

		if (item instanceof AssemblyItem assemblyItem) {
			AssemblyType assembly = assemblyItem.getAssembly();

			while (RustClient.ROTATE_KEY.wasPressed()) {
				if (hadLast) {
					rotation = rotation.next();
					PlayerRotationView.of(client.player).rust_setAssemblyRotation(rotation.getBlockRotation());
					rotationUpdate = true;
				}
			}

			if (rotationUpdate) {
				client.player.playSound(SoundEvents.ENTITY_ITEM_FRAME_ROTATE_ITEM, SoundCategory.BLOCKS, 0.9f, 0.8f);

				RustPackets.ROTATION.send(rotation.getBlockRotation(), buffer -> {
					ClientSidePacketRegistry.INSTANCE.sendToServer(AssemblyRotationC2SPacket.ID, buffer);
				});
			}

			if (result instanceof BlockHitResult hit && result.getType() == HitResult.Type.BLOCK) {
				final BlockPos pos = AssemblyType.getPlacementPosition(context.world(), hit);
				final BakedModel model = supplier.get();

				boolean valid = assembly.isValid(context.world(), pos, rotation.getBlockRotation());
				float wave = (float) (Math.sin(GlfwUtil.getTime() * 2.8f) * 0.5) + 0.5f;

				float r = valid ? 1 : 0.85f;
				float g = valid ? 1 : 0.33f;
				float b = valid ? 1 : 0.33f;

				if (hadLast) {
					this.r = (this.r * 0.91f + r * 0.09f);
					this.g = (this.g * 0.91f + g * 0.09f);
					this.b = (this.b * 0.91f + b * 0.09f);
				} else {
					this.r = r;
					this.g = g;
					this.b = b;
					hadLast = true;
				}

				float a = wave * 0.2f + 0.65f;

				if (model != null) {
					final MatrixStack matrices = context.matrixStack();
					final Camera camera = context.camera();
					final RenderLayer layer = OutlineRenderLayer.getOutlineLayer(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
					final VertexConsumer buffer = context.consumers().getBuffer(layer);

					matrices.push();
					matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
					matrices.translate(pos.getX() + rotation.x, pos.getY(), pos.getZ() + rotation.z);
					matrices.multiply(RenderHelper.getDegreesQuaternion(UP, rotation.angle));

					RenderHelper.setTint(this.r, this.g, this.b, a);
					RenderHelper.renderModel(model, context.world(), pos, buffer, matrices, 0, 42);

					matrices.pop();
				}
			}
		}
	}

}
