package net.darktree.rust.render.model;

import net.darktree.rust.Rust;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.util.Identifier;

public class RustModels {

	public static final Identifier TEST = Rust.id("block/test");

	public static void init() {
		Identifier assembly = Rust.id("block/dynamic_assembly_model");

		ModelLoadingPlugin.register(plugin -> {
			plugin.addModels(TEST);

			plugin.modifyModelOnLoad().register((original, context) -> {
				if (context.id().getNamespace().equals(Rust.NAMESPACE)) {
					Rust.LOGGER.info(context.id().toString());
				}

				if(context.id().equals(assembly)) {
					Rust.LOGGER.info("Creating AssemblyModel...");
					return new AssemblyModel();
				}

				return original;
			});
		});
	}

}
