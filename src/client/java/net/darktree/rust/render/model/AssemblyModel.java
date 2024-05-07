package net.darktree.rust.render.model;

import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.assembly.AssemblyType;
import net.darktree.rust.block.AssemblyBlock;
import net.darktree.rust.block.entity.AssemblyBlockEntity;
import net.darktree.rust.util.BlockUtil;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class AssemblyModel implements UnbakedModel, BakedModel, FabricBakedModel {

	private static final Map<AssemblyType, Map<BlockRotation, BakedModel>> TYPES = new IdentityHashMap<>();

	private static Map<BlockRotation, BakedModel> bakeRotationMapFor(Baker baker, UnbakedModel model, Function<SpriteIdentifier, Sprite> textures, Identifier id) {
		Map<BlockRotation, BakedModel> map = new EnumMap<>(BlockRotation.class);

		map.put(BlockRotation.NONE, model.bake(baker, textures, ModelRotation.X0_Y0, id));
		map.put(BlockRotation.CLOCKWISE_90, model.bake(baker, textures, ModelRotation.X0_Y90, id));
		map.put(BlockRotation.CLOCKWISE_180, model.bake(baker, textures, ModelRotation.X0_Y180, id));
		map.put(BlockRotation.COUNTERCLOCKWISE_90, model.bake(baker, textures, ModelRotation.X0_Y270, id));

		return map;
	}

	public static void bakeUnderlyingModels(Baker baker, BiFunction<Identifier, SpriteIdentifier, Sprite> loader) {
		RustModels.ASSEMBLIES.forEach((type, value) -> TYPES.put(type, bakeRotationMapFor(baker, baker.getOrLoadModel(value), id -> loader.apply(value, id), value)));
	}

	public static BakedModel getModelFor(AssemblyType type, BlockRotation rotation) {
		return TYPES.get(type).get(rotation);
	}

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
		return TYPES.values().stream().findAny().map(map -> map.get(BlockRotation.NONE)).orElseThrow().getParticleSprite();
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
		return RustModels.ASSEMBLIES.values();
	}

	@Override
	public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

	}

	@Nullable
	@Override
	public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textures, ModelBakeSettings rotations, Identifier id) {
		return this;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		if (state.get(AssemblyBlock.CENTRAL)) {
			BlockUtil.getBlockEntity(blockView, pos, AssemblyBlockEntity.class).flatMap(AssemblyBlockEntity::getAssembly).ifPresent(instance -> {
				getModelFor(instance.getType(), instance.getRotation()).emitBlockQuads(blockView, state, pos, randomSupplier, context);
			});
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		// items will not use this model
	}

}
