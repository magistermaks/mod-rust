package net.darktree.rust.mixin;

import net.darktree.rust.util.duck.PlayerRotationView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.BlockRotation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerRotationView {

	@Unique
	BlockRotation assemblyRotation = BlockRotation.NONE;

	@Override
	public BlockRotation rust_getAssemblyRotation() {
		return this.assemblyRotation;
	}

	@Override
	public void rust_setAssemblyRotation(BlockRotation rotation) {
		this.assemblyRotation = rotation;
	}

}
