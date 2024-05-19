package net.darktree.rust.util.duck;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.function.Function;

public interface UnbakedMultiblockView {

	static UnbakedMultiblockView of(JsonUnbakedModel model) {
		return (UnbakedMultiblockView) model;
	}

	Map<BlockPos, BakedModel> rust_bakeModelMap(Baker baker, Function<SpriteIdentifier, Sprite> textures, ModelBakeSettings rotation, Identifier id);

}
