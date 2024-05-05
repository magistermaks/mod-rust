package net.darktree.rust.util.duck;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.BlockRotation;

public interface PlayerRotationView {

	static PlayerRotationView of(PlayerEntity player) {
		return (PlayerRotationView) player;
	}

	BlockRotation rust_getAssemblyRotation();
	void rust_setAssemblyRotation(BlockRotation rotation);

}
