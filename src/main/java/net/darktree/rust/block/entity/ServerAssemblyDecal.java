package net.darktree.rust.block.entity;

import net.darktree.rust.assembly.AssemblyInstance;
import net.minecraft.world.World;

public interface ServerAssemblyDecal {

	void tick(World world, AssemblyBlockEntity entity, AssemblyInstance instance);

	void fetch(AssemblyInstance instance);

}
