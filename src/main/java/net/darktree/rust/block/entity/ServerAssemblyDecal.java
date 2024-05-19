package net.darktree.rust.block.entity;

import net.darktree.rust.assembly.AssemblyInstance;
import net.minecraft.world.World;

public interface ServerAssemblyDecal {

	DecalType<?> getType();

	// TODO
	void tick(World world, AssemblyBlockEntity entity, AssemblyInstance instance);

}
