package net.darktree.rust.render.model;

import net.darktree.rust.Rust;
import net.darktree.rust.assembly.AssemblyType;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.util.Identifier;

import java.util.IdentityHashMap;
import java.util.Map;

public class RustModels {

	public static final Identifier BAKER_DUMMY_ID = Rust.id("i_like_freshly_baked_bread");
	public static final Map<AssemblyType, Identifier> ASSEMBLIES = new IdentityHashMap<>();

	public static void init() {
		final Identifier assembly = Rust.id("block/dynamic_assembly_model");

		// create a map of all the assemblies types to their respective models
		Rust.ASSEMBLY_REGISTRY.getEntrySet().forEach(entry -> {
			Identifier id = entry.getKey().getValue();
			AssemblyType type = entry.getValue();

			ASSEMBLIES.put(type, new Identifier(id.getNamespace(), "assembly/" + id.getPath()));
		});

		ModelLoadingPlugin.register(plugin -> {
			plugin.modifyModelOnLoad().register((original, context) -> {
				if(context.id().equals(assembly)) {
					return new AssemblyModel();
				}

				return original;
			});
		});
	}

}
