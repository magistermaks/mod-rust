package net.darktree.rust.particle;

import net.darktree.lumberjack.particle.RenderedParticle;
import net.darktree.rust.render.ArcRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class ElectricParticle extends RenderedParticle {

	protected ElectricParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, vx, vy, vz);
		this.maxAge *= 2;
	}

	@Override
	public void render(VertexConsumerProvider.Immediate immediate, MatrixStack matrices, float delta) {
		final float scale = (float) (this.maxAge - this.age) / (float) this.maxAge;

		VertexConsumer buffer = immediate.getBuffer(RenderLayer.getLightning());
		ArcRenderer.renderNoise(matrices, buffer, this.seed + this.age, 8, scale * 0.5f, 0, 0, 0);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		public Factory(SpriteProvider spriteProvider) {}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new ElectricParticle(clientWorld, d, e, f, g, h, i);
		}
	}

}
