package net.darktree.rust.item;

import net.darktree.rust.assembly.AssemblyType;
import net.darktree.rust.util.duck.PlayerRotationView;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class AssemblyItem extends Item {

	private final AssemblyType assembly;

	public AssemblyItem(Settings settings, AssemblyType assembly) {
		super(settings);
		this.assembly = assembly;
	}

	public AssemblyType getAssembly() {
		return assembly;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockRotation rotation = BlockRotation.NONE;
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = AssemblyType.getPlacementPosition(world, context.getHitResult());
		ItemStack stack = context.getStack();

		if (player instanceof PlayerRotationView view) {
			rotation = view.rust_getAssemblyRotation();
		}

		if (assembly.isValid(world, pos, rotation)) {

			if (player instanceof ServerPlayerEntity serverPlayer) {
				Criteria.PLACED_BLOCK.trigger(serverPlayer, context.getBlockPos(), stack);
			}

			assembly.place(world, pos, rotation);

			BlockSoundGroup group = assembly.getSoundGroup();
			world.playSound(player, pos, group.getPlaceSound(), SoundCategory.BLOCKS, (group.getVolume() + 1.0f) / 2.0f, group.getPitch() * 0.8f);
			world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(player, null));

			if (player == null || !player.getAbilities().creativeMode) {
				stack.decrement(1);
			}

			return ActionResult.CONSUME;
		}

		return ActionResult.PASS;
	}

//	@Override
//	public String getTranslationKey() {
//		return this.getBlock().getTranslationKey();
//	}

//	@Override
//	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
//		super.appendTooltip(stack, world, tooltip, context);
//		this.getBlock().appendTooltip(stack, world, tooltip, context);
//	}

//	@Override
//	public void onItemEntityDestroyed(ItemEntity entity) {
//		ItemStack itemStack;
//		NbtCompound nbtCompound;
//		if (this.block instanceof ShulkerBoxBlock && (nbtCompound = BlockItem.getBlockEntityNbt(itemStack = entity.getStack())) != null && nbtCompound.contains("Items", NbtElement.LIST_TYPE)) {
//			NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
//			ItemUsage.spawnItemContents(entity, nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt));
//		}
//	}

}
