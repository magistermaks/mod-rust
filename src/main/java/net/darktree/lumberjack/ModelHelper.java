package net.darktree.lumberjack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;

public class ModelHelper {

	@Environment(EnvType.CLIENT)
	public static BakedModel getModel(Identifier id) {
		BakedModelManager manager = MinecraftClient.getInstance().getBakedModelManager();
		return BakedModelManagerHelper.getModel(manager, id);
	}

	@Environment(EnvType.CLIENT)
	public static void loadModels(Identifier... ids) {
		getModelLoader().add(ids).submit();
	}

	@Environment(EnvType.CLIENT)
	public static ModelLoader getModelLoader() {
		return new ModelLoader();
	}

	static class ModelLoader {

		private final ArrayList<Identifier> models = new ArrayList<>();

		public ModelLoader add(Identifier... ids) {
			models.addAll(Arrays.asList(ids));
			return this;
		}

		@Environment(EnvType.CLIENT)
		public void submit() {
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
				for(Identifier id : this.models) out.accept(id);
			});
		}

	}

}
