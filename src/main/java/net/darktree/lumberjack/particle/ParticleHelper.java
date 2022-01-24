package net.darktree.lumberjack.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ParticleHelper {

	private final static HashMap<Identifier, List<Identifier>> particleSprites = new HashMap<>();

	public static DefaultParticleType register(Identifier particle, boolean always, Identifier... sprites) {
		ArrayList<Identifier> paths = new ArrayList<>();

		for(Identifier sprite : sprites) {
			paths.add( new Identifier(sprite.getNamespace(), "particle/" + sprite.getPath()) );
		}

		particleSprites.put(particle, paths);
		return Registry.register(Registry.PARTICLE_TYPE, particle, FabricParticleTypes.simple(always));
	}

	@ApiStatus.Internal
	public static List<Identifier> getSpritesAndForget(Identifier identifier) {
		return particleSprites.remove(identifier);
	}

}
