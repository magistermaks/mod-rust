package net.darktree.rust.assembly.decal;

import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.block.AssemblyBlockEntity;
import net.minecraft.world.World;

public interface ServerAssemblyDecal {

	DecalType<?, ?> getType();

	// TODO
	void tick(World world, AssemblyBlockEntity entity, AssemblyInstance instance);

}
