package net.darktree.rust.render.block;

import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.block.AssemblyBlockEntity;
import net.darktree.rust.assembly.decal.ServerAssemblyDecal;
import net.darktree.rust.render.decal.ClientAssemblyDecal;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Optional;

public class AssemblyBlockEntityRenderer implements BlockEntityRenderer<AssemblyBlockEntity> {

	public AssemblyBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

	}

	@Override
	public void render(AssemblyBlockEntity entity, float delta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

		Optional<AssemblyInstance> optional = entity.getAssembly();

		if (optional.isPresent()) {

			final AssemblyInstance instance = optional.get();
			final VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getSolid());

			for (ServerAssemblyDecal server : instance.getDecalList(entity.getModelOffsetKey())) {
				ClientAssemblyDecal client = (ClientAssemblyDecal) server;

				client.render(entity.getWorld(), entity.getPos(), instance, delta, matrices, consumer, overlay, 1, 1, 1, 1);
			}

		}

	}

}
