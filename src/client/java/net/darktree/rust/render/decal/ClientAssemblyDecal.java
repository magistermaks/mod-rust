package net.darktree.rust.render.decal;

import net.darktree.rust.assembly.AssemblyRenderView;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ClientAssemblyDecal {

	void render(World world, BlockPos pos, AssemblyRenderView instance, float delta, MatrixStack matrices, VertexConsumer consumer, int overlay, float r, float g, float b, float a);

}
