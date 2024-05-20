package net.darktree.rust.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.darktree.rust.RustRegistries;
import net.darktree.rust.assembly.decal.ConfiguredDecal;
import net.darktree.rust.assembly.decal.DecalConfig;
import net.darktree.rust.assembly.decal.DecalType;
import net.darktree.rust.render.model.RustModels;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class JsonDecalBlob {

	private static final String DECAL_KEY = "decal";
	private static final String BLOCK_KEY = "block";
	private static final String CONFIG_KEY = "config";

	private final DecalType<?, ?> decal;
	private final BlockPos block;
	private final DecalConfig config;

	public JsonDecalBlob(DecalType<?, ?> decal, BlockPos block, DecalConfig config) {
		this.decal = decal;
		this.block = block;
		this.config = config;

		RustModels.MODELS.addAll(config.getModels());
	}

	private static BlockPos getBlockPos(JsonArray array) {
		if (array.size() != 3) {
			throw new RuntimeException("Expected block position to have 3 components, but it had " + array.size() + "!");
		}

		return new BlockPos(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
	}

	public static JsonDecalBlob of(JsonObject json) {
		Identifier id = Identifier.tryParse(json.get(DECAL_KEY).getAsString());
		DecalType<?, ?> decal = RustRegistries.DECAL.get(id);
		BlockPos pos = getBlockPos(json.getAsJsonArray(BLOCK_KEY));

		if (decal == null) {
			throw new RuntimeException("No such decal '" + id + "'!");
		}

		return new JsonDecalBlob(decal, pos, decal.createConfig(json.has(CONFIG_KEY) ? json.getAsJsonObject(CONFIG_KEY) : new JsonObject()));
	}

	public BlockPos getOffset() {
		return block;
	}

	public ConfiguredDecal<?, ?> asConfiguredDecal() {
		return this.decal.getConfigured(this.config);
	}

}
