package net.darktree.rust.assembly.decal;

import net.darktree.rust.assembly.AssemblyType;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class AssemblyDecalManager  {

	private static final Map<AssemblyType, Map<BlockPos, List<ConfiguredDecal<?, ?>>>> ATTACHMENTS = new IdentityHashMap<>();

	public static <T extends ServerAssemblyDecal> void register(AssemblyType type, BlockPos offset, ConfiguredDecal<?, ?> decal) {
		ATTACHMENTS.computeIfAbsent(type, key -> new HashMap<>()).computeIfAbsent(offset, key -> new ArrayList<>()).add(decal);
	}

	public static Map<BlockPos, List<ConfiguredDecal<?, ?>>> getDecals(AssemblyType type) {
		return ATTACHMENTS.computeIfAbsent(type, key -> Collections.emptyMap());
	}

	public static void forgetAll() {
		ATTACHMENTS.clear();
	}

}
