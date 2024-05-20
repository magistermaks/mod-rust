package net.darktree.rust.json;

import net.darktree.rust.Rust;
import net.darktree.rust.RustRegistries;
import net.darktree.rust.assembly.AssemblyType;
import net.darktree.rust.assembly.decal.AssemblyDecalManager;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class JsonAssemblyLoader implements SimpleResourceReloadListener<Map<AssemblyType, JsonAssemblyBlob>> {

	private static final Identifier ID = Rust.id("assembly_definition");

	@Override
	public CompletableFuture<Map<AssemblyType, JsonAssemblyBlob>> load(ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			Map<AssemblyType, JsonAssemblyBlob> map = new IdentityHashMap<>();

			RustRegistries.ASSEMBLY.getEntrySet().forEach(entry -> {
				Identifier id = entry.getKey().getValue();
				AssemblyType type = entry.getValue();

				try {
					Identifier definition = new Identifier(id.getNamespace(), "assemblies/" + id.getPath() + ".json");
					Resource resource = manager.getResourceOrThrow(definition);

					try (BufferedReader reader = resource.getReader()) {
						map.put(type, JsonAssemblyBlob.of(type, JsonHelper.deserialize(reader)));
					}

				} catch (Exception e) {
					Rust.LOGGER.error("Loading failed for assembly definition '{}'", id, e);
					map.put(type, JsonAssemblyBlob.missing(type));
				}
			});

			return map;
		});
	}

	@Override
	public CompletableFuture<Void> apply(Map<AssemblyType, JsonAssemblyBlob> data, ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
			AssemblyDecalManager.forgetAll();

			data.forEach((type, jsonAssemblyBlob) -> {
				jsonAssemblyBlob.getDecals().forEach(decal -> {
					AssemblyDecalManager.register(type, decal.getOffset(), decal.asConfiguredDecal());
				});
			});
		});
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}

}
