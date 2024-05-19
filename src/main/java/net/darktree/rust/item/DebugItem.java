package net.darktree.rust.item;

import net.darktree.rust.util.DebugAppender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.state.State;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DebugItem extends Item implements DebugAppender {

	public DebugItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {

		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		PlayerEntity player = context.getPlayer();

		if (!world.isClient && player != null) {

			List<Text> lines = new ArrayList<>();
			getDebugReport(world, pos, lines);

			for (Text text : lines) {
				player.sendMessage(text);
			}

		}

		return ActionResult.success(world.isClient);
	}

	@Override
	public void getDebugReport(World world, BlockPos pos, List<Text> lines) {

		// spacer
		lines.add(Text.literal(""));

		{ // position
			Text tx = Text.literal(pos.getX() + "").formatted(Formatting.AQUA);
			Text ty = Text.literal(pos.getY() + "").formatted(Formatting.AQUA);
			Text tz = Text.literal(pos.getZ() + "").formatted(Formatting.AQUA);

			lines.add(Text.literal("Target: ").append(tx).append(", ").append(ty).append(", ").append(tz));
		}

		{ // block
			Block block = world.getBlockState(pos).getBlock();

			Text tb = Text.literal(Registries.BLOCK.getId(block).toString()).formatted(Formatting.AQUA);
			Text tc = Text.literal(block.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(block)));

			lines.add(Text.literal("Block: ").append(tb).append(" (").append(tc).append(")"));
		}

		{ // state
			BlockState state = world.getBlockState(pos);

			Text ts = state.getEntries().entrySet().stream()
					.map(State.PROPERTY_MAP_PRINTER)
					.map(str -> Text.literal(str).formatted(Formatting.AQUA))
					.reduce((a, b) -> Text.literal("").append(a).append(", ").append(b))
					.orElseGet(() -> Text.literal("This block has no states").formatted(Formatting.GRAY));

			lines.add(Text.literal("State: ").append(ts));
		}

		{ // block entity
			BlockEntity entity = world.getBlockEntity(pos);

			Text line;

			if (entity != null) {
				Text te = Text.literal(Registries.BLOCK_ENTITY_TYPE.getId(entity.getType()).toString()).formatted(Formatting.AQUA);
				Text tc = Text.literal(entity.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(entity)));

				line = Text.literal("").append(te).append(" (").append(tc).append(")");
			} else {
				line = Text.literal("This block has no entity").formatted(Formatting.GRAY);
			}

			lines.add(Text.literal("Entity: ").append(line));

			if (entity instanceof DebugAppender appender) {
				appender.getDebugReport(world, pos, lines);
			}
		}



	}

}
