package net.darktree.lumberjack.particle;

import net.darktree.lumberjack.mixin.ParticleAccessor;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ParticleHelper {

	// make it not public
	public final static HashMap<Identifier, List<Identifier>> particleSprites = new HashMap<>();

	public static DefaultParticleType register(Identifier particle, boolean always, Identifier... sprites) {
		ArrayList<Identifier> paths = new ArrayList<>();

		for(Identifier sprite : sprites) {
			paths.add( new Identifier(sprite.getNamespace(), "particle/" + sprite.getPath()) );
		}

		particleSprites.put(particle, paths);
		return Registry.register(Registry.PARTICLE_TYPE, new Identifier("rust", "electric"), FabricParticleTypes.simple(always));
	}

	@Deprecated
	public static MatrixStack getMatrices(Particle particle, Camera camera, float delta) {
		MatrixStack matrices = new MatrixStack();
		Vec3d origin = camera.getPos();
		ParticleAccessor accessor = (ParticleAccessor) particle;

		double x = /*MathHelper.lerp(delta, accessor.getPrevPosX(), accessor.getX())*/ accessor.getX() - origin.getX();
		double y = /*MathHelper.lerp(delta, accessor.getPrevPosY(), accessor.getY())*/ accessor.getY() - origin.getY();
		double z = /*MathHelper.lerp(delta, accessor.getPrevPosZ(), accessor.getZ())*/ accessor.getZ() - origin.getZ();

		matrices.translate(x, y, z);
		matrices.scale(0.5F, -0.5F, 0.5F);
		matrices.translate(0, -1, 0);

		return matrices;
	}

}
