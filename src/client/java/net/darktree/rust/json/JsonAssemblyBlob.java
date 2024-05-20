package net.darktree.rust.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darktree.rust.assembly.AssemblyType;
import net.darktree.rust.render.model.RustModels;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;

import java.util.List;

public class JsonAssemblyBlob {

	private static final String MODEL_KEY = "model";
	private static final String DECALS_KEY = "decals";

	private final List<JsonDecalBlob> decals;

	private JsonAssemblyBlob(AssemblyType type, Identifier model, List<JsonDecalBlob> decals) {
		this.decals = decals;

		RustModels.MODELS.add(model);
		RustModels.ASSEMBLIES.put(type, model);
	}

	private static List<JsonDecalBlob> getDecals(JsonArray decals) {
		return decals.asList().stream().map(JsonElement::getAsJsonObject).map(JsonDecalBlob::of).toList();
	}

	public static JsonAssemblyBlob of(AssemblyType type, JsonObject json) {
		return new JsonAssemblyBlob(type, Identifier.tryParse(json.get(MODEL_KEY).getAsString()), getDecals(json.get(DECALS_KEY).getAsJsonArray()));
	}

	public static JsonAssemblyBlob missing(AssemblyType type) {
		return new JsonAssemblyBlob(type, ModelLoader.MISSING_ID, List.of());
	}

	public List<JsonDecalBlob> getDecals() {
		return decals;
	}

}
