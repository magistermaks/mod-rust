package net.darktree.rust.assembly.decal;

public class ConfiguredDecal<T extends ServerAssemblyDecal, C extends DecalConfig> {

	private final DecalType<T, C> type;
	private final C config;

	public ConfiguredDecal(DecalType<T, C> type, C config) {
		this.type = type;
		this.config = config;
	}

	public T create() {
		return type.create(config);
	}

}
