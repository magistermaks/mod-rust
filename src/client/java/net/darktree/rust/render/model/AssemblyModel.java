package net.darktree.rust.render.model;

import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.darktree.rust.util.BlockUtil;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class AssemblyModel implements UnbakedModel, BakedModel, FabricBakedModel {

//	private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
//			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft:block/furnace_front_on")),
//			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft:block/furnace_top"))
//	};
//	private final Sprite[] sprites = new Sprite[SPRITE_IDS.length];

	// Some constants to avoid magic numbers, these need to match the SPRITE_IDS
//	private static final int SPRITE_SIDE = 0;
//	private static final int SPRITE_TOP = 1;
//
//	private Mesh mesh;

	EnumMap<BlockRotation, BakedModel> map = new EnumMap<>(BlockRotation.class);

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return List.of();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getParticleSprite() {
		return map.get(BlockRotation.NONE).getParticleSprite();
	}

	@Override
	public ModelTransformation getTransformation() {
		return null;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return null;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return List.of(RustModels.TEST);
	}

	@Override
	public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

	}

	@Nullable
	@Override
	public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		// Get the sprites
//		for(int i = 0; i < SPRITE_IDS.length; ++i) {
//			sprites[i] = textureGetter.apply(SPRITE_IDS[i]);
//		}

		// Build the mesh using the Renderer API
//		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
//		MeshBuilder builder = renderer.meshBuilder();
//		QuadEmitter emitter = builder.getEmitter();

		//

		UnbakedModel model = baker.getOrLoadModel(RustModels.TEST);

		map.put(BlockRotation.NONE, model.bake(baker, textureGetter, ModelRotation.X0_Y0, modelId));
		map.put(BlockRotation.CLOCKWISE_90, model.bake(baker, textureGetter, ModelRotation.X0_Y90, modelId));
		map.put(BlockRotation.CLOCKWISE_180, model.bake(baker, textureGetter, ModelRotation.X0_Y180, modelId));
		map.put(BlockRotation.COUNTERCLOCKWISE_90, model.bake(baker, textureGetter, ModelRotation.X0_Y270, modelId));

		//for (BakedQuad quad : model.getQuads(, ))

//		for(Direction direction : Direction.values()) {
//			// UP and DOWN share the Y axis
//			int spriteIdx = direction == Direction.UP || direction == Direction.DOWN ? SPRITE_TOP : SPRITE_SIDE;
//			// Add a new face to the mesh
//			emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
//			// Set the sprite of the face, must be called after .square()
//			// We haven't specified any UV coordinates, so we want to use the whole texture. BAKE_LOCK_UV does exactly that.
//			emitter.spriteBake(sprites[spriteIdx], MutableQuadView.BAKE_LOCK_UV);
//			// Enable texture usage
//			emitter.color(-1, -1, -1, -1);
//			// Add the quad to the mesh
//			emitter.emit();
//		}

		//mesh = builder.build();

		return this;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		if (state.get(AssemblyBlock.CENTRAL)) {
			BlockRotation rotation = BlockUtil.getBlockEntity(blockView, pos, AssemblyBlockEntity.class).map(AssemblyBlockEntity::getRotation).orElse(BlockRotation.NONE);
			map.get(rotation).emitBlockQuads(blockView, state, pos, randomSupplier, context);
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		map.get(BlockRotation.NONE).emitItemQuads(stack, randomSupplier, context);
	}

}
