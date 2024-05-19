package net.darktree.rust.util;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface DebugAppender {

	void getDebugReport(World world, BlockPos pos, List<Text> lines);

}
