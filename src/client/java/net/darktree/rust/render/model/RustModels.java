package net.darktree.rust.render.model;

import net.darktree.rust.Rust;
import net.darktree.rust.RustRegistries;
import net.darktree.rust.assembly.AssemblyType;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class RustModels {

	public static final List<Identifier> MODELS = new ArrayList<>();
	public static final Identifier BAKER_DUMMY_ID = Rust.id("i_like_freshly_baked_bread");
	public static final Map<AssemblyType, Identifier> ASSEMBLIES = new IdentityHashMap<>();

	public static void init() {
		final Identifier assembly = Rust.id("block/dynamic_assembly_model");

		ModelLoadingPlugin.register(plugin -> {
			plugin.addModels(MODELS);

			plugin.modifyModelOnLoad().register((original, context) -> {
				if(context.id().equals(assembly)) {
					return new AssemblyModel();
				}

				return original;
			});
		});
	}

}
