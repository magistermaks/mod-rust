package net.darktree.rust.assembly.decal;

import com.google.gson.JsonObject;

import java.util.function.Function;

public final class DecalType <T extends ServerAssemblyDecal, C extends DecalConfig> {

	private final Function<C, T> constructor;
	private final Function<JsonObject, C> configurator;

	public DecalType(Function<C, T> supplier, Function<JsonObject, C> configurator) {
		this.constructor = supplier;
		this.configurator = configurator;
	}

	public T create(C config) {
		return constructor.apply(config);
	}

	public C createConfig(JsonObject json) {
		return configurator.apply(json);
	}

	@SuppressWarnings("unchecked")
	public ConfiguredDecal<T, C> getConfigured(DecalConfig config) {
		return new ConfiguredDecal<>(this, (C) config);
	}

}
