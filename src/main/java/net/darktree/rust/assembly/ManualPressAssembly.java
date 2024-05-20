package net.darktree.rust.assembly;

import net.darktree.rust.Rust;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ManualPressAssembly extends AssemblyInstance {

	private double velocity = 0;
	private double angle = 0;

	public ManualPressAssembly(BlockRotation rotation, BlockPos origin) {
		super(Rust.MANUAL_PRESS, rotation, origin);
	}

	public void onUse(World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		velocity += (hit.getSide().getDirection() == Direction.AxisDirection.NEGATIVE ? +12 : -12);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		velocity = (velocity - Math.sin(angle * 0.01745329)) * (1 - 0.21 / MathHelper.clamp(1 + Math.abs(velocity), 1, 3));
		angle = angle + velocity * 1.98;

		constants.get(Rust.CRANK).push(angle);
	}

}
