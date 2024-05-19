package net.darktree.rust.block.entity;

import net.darktree.rust.assembly.AssemblyType;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class AssemblyDecalManager  {

	private static final Map<AssemblyType, Map<BlockPos, List<DecalType<? extends ServerAssemblyDecal>>>> attachments = new IdentityHashMap<>();

	public static <T extends ServerAssemblyDecal> void register(AssemblyType type, List<DecalPlacement<T>> placements) {
		placements.forEach(placement -> register(type, placement));
	}

	public static <T extends ServerAssemblyDecal> void register(AssemblyType type, DecalPlacement<T> placement) {
		attachments.computeIfAbsent(type, key -> new HashMap<>()).computeIfAbsent(placement.getOffset(), key -> new ArrayList<>()).add(placement.getType());
	}

	public static Map<BlockPos, List<DecalType<? extends ServerAssemblyDecal>>> getDecals(AssemblyType type) {
		return attachments.computeIfAbsent(type, key -> Collections.emptyMap());
	}

}
