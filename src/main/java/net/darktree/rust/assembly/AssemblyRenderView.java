package net.darktree.rust.assembly;

import net.darktree.rust.block.entity.DecalPushConstant;
import net.minecraft.util.BlockRotation;

public interface AssemblyRenderView {

	DecalPushConstant getDecalPushConstant(DecalPushConstant.Type type);
	BlockRotation getRotation();

}
