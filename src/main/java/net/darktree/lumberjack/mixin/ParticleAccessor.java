package net.darktree.lumberjack.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {

	@Accessor("prevPosX")
	double getPrevPosX();

	@Accessor("prevPosY")
	double getPrevPosY();

	@Accessor("prevPosZ")
	double getPrevPosZ();

	@Accessor("x")
	double getX();

	@Accessor("y")
	double getY();

	@Accessor("z")
	double getZ();

}