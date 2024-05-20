package net.darktree.rust.render.decal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darktree.rust.RustClient;
import net.darktree.rust.RustRegistries;
import net.darktree.rust.assembly.AssemblyInstance;
import net.darktree.rust.assembly.AssemblyRenderView;
import net.darktree.rust.assembly.decal.*;
import net.darktree.rust.block.AssemblyBlockEntity;
import net.darktree.rust.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Objects;

public class RevolvingDecal implements ServerAssemblyDecal, ClientAssemblyDecal {

	protected final Config config;

	public RevolvingDecal(Config config) {
		this.config = config;
	}

	protected final float rotationToAngle(BlockRotation rotation) {
		return switch (rotation) {
			case NONE -> 0;
			case CLOCKWISE_90 -> 90;
			case CLOCKWISE_180 -> 180;
			case COUNTERCLOCKWISE_90 -> 270;
		};
	}

	@Override
	public DecalType<RevolvingDecal, Config> getType() {
		return RustClient.REVOLVING;
	}

	@Override
	public void tick(World world, AssemblyBlockEntity entity, AssemblyInstance instance) {

	}

	@Override
	public void render(World world, BlockPos pos, AssemblyRenderView instance, float delta, MatrixStack matrices, VertexConsumer consumer, int overlay, float r, float g, float b, float a) {
		matrices.push();

		final Vector3f va = config.absolute_offset;
		final Vector3f vr = config.relative_offset;
		final Vector4f vc = config.tint;

		matrices.translate(0.5, 0.5, 0.5);
		matrices.translate(va.x, va.y, va.z);

		if (config.apply_block_rotation) {
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(360 - rotationToAngle(instance.getRotation())));
		}

		DecalPushConstant constant = instance.getDecalPushConstant(config.uniform);
		matrices.multiply(config.axis_of_rotation.rotationDegrees((float) constant.getLinear(delta)));

		matrices.translate(-0.5, -0.5, -0.5);
		matrices.translate(vr.x, vr.y, vr.z);

		RenderHelper.setTint(r * vc.x, g * vc.y, b * vc.z, a * vc.w);

		BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(config.model);
		RenderHelper.renderModel(model, world, pos, consumer, matrices, overlay, 42);

		matrices.pop();
	}

	public static class Config extends DecalConfig {

		public final boolean apply_block_rotation;
		public final RotationAxis axis_of_rotation;
		public final Vector3f absolute_offset;
		public final Vector3f relative_offset;
		public final Vector4f tint;
		public final DecalPushConstant.Type uniform;
		public final Identifier model;

		protected final JsonElement getAndAssert(JsonObject json, String key) {
			return Objects.requireNonNull(json.get(key), "Required property '" + key + "' is missing from decal config!");
		}

		protected final boolean parseBool(JsonObject json, String key, Boolean fallback) {
			return fallback == null || json.has(key) ? getAndAssert(json, key).getAsBoolean() : fallback;
		}

		protected final RotationAxis parseAxis(JsonObject json, String key, RotationAxis fallback) {
			if (fallback != null && !json.has(key)) {
				return fallback;
			}

			String axis = getAndAssert(json, key).getAsString();

			return switch (axis) {
				case "x" -> RotationAxis.POSITIVE_X;
				case "y" -> RotationAxis.POSITIVE_Y;
				case "z" -> RotationAxis.POSITIVE_Z;
				default -> throw new RuntimeException("Invalid axis '" + axis + "' in property '" + key + "!");
			};
		}

		protected final Vector3f parseVector3f(JsonObject json, String key, Vector3f fallback) {
			if (fallback != null && !json.has(key)) {
				return fallback;
			}

			JsonArray array = getAndAssert(json, key).getAsJsonArray();

			if (array.size() != 3) {
				throw new RuntimeException("Expected 3 vector components, but got " + array.size() + " in property '" + key + "!");
			}

			return new Vector3f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
		}

		protected final Vector4f parseVector4f(JsonObject json, String key, Vector4f fallback) {
			if (fallback != null && !json.has(key)) {
				return fallback;
			}

			JsonArray array = getAndAssert(json, key).getAsJsonArray();

			if (array.size() != 4) {
				throw new RuntimeException("Expected 4 vector components, but got " + array.size() + " in property '" + key + "!");
			}

			return new Vector4f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat(), array.get(3).getAsFloat());
		}

		protected final DecalPushConstant.Type parseDecalPushConstant(JsonObject json, String key, DecalPushConstant.Type fallback) {
			if (fallback != null && !json.has(key)) {
				return fallback;
			}

			Identifier id = Identifier.tryParse(getAndAssert(json, key).getAsString());
			DecalPushConstant.Type type = RustRegistries.CONSTANT.get(id);

			if (type == null) {
				throw new RuntimeException("Unknown decal push constant '" + id + "' in property '" + key + "!");
			}

			return type;
		}

		protected final Identifier parseIdentifier(JsonObject json, String key, Identifier fallback) {
			if (fallback != null && !json.has(key)) {
				return fallback;
			}

			return Identifier.tryParse(getAndAssert(json, key).getAsString());
		}

		public Config(JsonObject json) {
			apply_block_rotation = parseBool(json, "apply_block_rotation", true);
			axis_of_rotation = parseAxis(json, "axis_of_rotation", null);
			absolute_offset = parseVector3f(json, "absolute_offset", new Vector3f(0, 0, 0));
			relative_offset = parseVector3f(json, "relative_offset", new Vector3f(0, 0, 0));
			tint = parseVector4f(json, "tint", new Vector4f(1, 1, 1, 1));
			uniform = parseDecalPushConstant(json, "uniform", null);
			model = parseIdentifier(json, "model", null);
		}

	}

}

