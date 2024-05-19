package net.darktree.rust.assembly;

import net.darktree.rust.block.entity.DecalPushConstant;
import net.minecraft.util.BlockRotation;

import java.util.function.Supplier;

public final class AssemblyFallbackRenderView implements AssemblyRenderView {

	private final Supplier<BlockRotation> supplier;

	public AssemblyFallbackRenderView(Supplier<BlockRotation> supplier) {
		this.supplier = supplier;
	}

	@Override
	public DecalPushConstant getDecalPushConstant(DecalPushConstant.Type type) {
		return type.getFallback();
	}

	@Override
	public BlockRotation getRotation() {
		return supplier.get();
	}

}
