package net.darktree.rust.mixin.client;

import net.darktree.rust.util.LazyHashMap;
import net.darktree.rust.util.duck.UnbakedMultiblockView;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * We use this mixin to split select baked models into parts
 * by the model elements depending on the placement of those parts in the model.
 * Model elements will be assigned to a block in which they lay, and returned
 * as a hash map. (so a part of a model that is one block above the main block will
 * be returned in the hashmap with a key BlockPos{0, 1, 0})
 */
@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin implements UnbakedMultiblockView {

	@Shadow
	public abstract SpriteIdentifier resolveSprite(String spriteName);

	@Shadow
	protected abstract ModelOverrideList compileOverrides(Baker baker, JsonUnbakedModel parent);

	@Shadow
	protected JsonUnbakedModel parent;

	@Shadow
	public abstract List<ModelElement> getElements();

	@Shadow
	public abstract BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId);

	@Unique
	ModelElement copyElementForOffset(ModelElement element, BlockPos offset) {

		// if we make no change then there is no need to copy
		if (offset.equals(BlockPos.ORIGIN)) {
			return element;
		}

		ModelElement copy = new ModelElement(new Vector3f(element.from), new Vector3f(element.to), element.faces, element.rotation, element.shade);

		// adjust position
		copy.from.sub(offset.getX() * 16, offset.getY() * 16, offset.getZ() * 16);
		copy.to.sub(offset.getX() * 16, offset.getY() * 16, offset.getZ() * 16);

		return copy;
	}

	@Override
	public Map<BlockPos, BakedModel> rust_bakeModelMap(Baker baker, Function<SpriteIdentifier, Sprite> textures, ModelBakeSettings settings, Identifier id) {
		JsonUnbakedModel self = (JsonUnbakedModel) (Object) this;;
		Sprite sprite = textures.apply(this.resolveSprite(JsonUnbakedModel.PARTICLE_KEY));

		LazyHashMap<BlockPos, BasicBakedModel.Builder> builders = new LazyHashMap<>(key -> {
			return new BasicBakedModel.Builder(self, this.compileOverrides(baker, parent), true).setParticle(sprite);
		});

		final BakedModel model = this.bake(baker, textures, settings, id);

		for (ModelElement modelElement : this.getElements()) {

			Vector3f from = modelElement.from;
			BlockPos origin = BlockPos.ofFloored(from.x / 16, from.y / 16, from.z / 16);
			ModelElement modelElementCopy = copyElementForOffset(modelElement, origin);

			for (Direction direction : modelElementCopy.faces.keySet()) {

				ModelElementFace elementFace = modelElementCopy.faces.get(direction);
				Sprite faceSprite = textures.apply(this.resolveSprite(elementFace.textureId));
				BakedQuad quad = JsonUnbakedModel.createQuad(modelElementCopy, elementFace, faceSprite, direction, settings, id);

				if (elementFace.cullFace == null) {
					builders.get(origin).addQuad(quad);
					continue;
				}

				builders.get(origin).addQuad(Direction.transform(settings.getRotation().getMatrix(), elementFace.cullFace), quad);
			}
		}

		Map<BlockPos, BakedModel> models = builders.map(HashMap::new, BasicBakedModel.Builder::build);
		models.put(null, model);

		return models;
	}

}
