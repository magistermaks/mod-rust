package net.darktree.rust.assembly.decal;

import net.minecraft.util.math.MathHelper;

public final class DecalPushConstant {

	private final Type type;
	private double previous;
	private double current;

	public DecalPushConstant(Type type, double initial) {
		this.type = type;
		this.previous = initial;
		this.current = initial;
	}

	public void push(double value) {
		previous = current;
		current = value;
	}

	public double getPrevious() {
		return previous;
	}

	public double getCurrent() {
		return current;
	}

	public double getLinear(double delta) {
		return MathHelper.lerp(delta, previous, current);
	}

	public Type getType() {
		return type;
	}

	public static class Type {

		private final double initial;
		private final DecalPushConstant fallback;

		private Type(double initial) {
			this.initial = initial;
			this.fallback = create();
		}

		public static Type of(double initial) {
			return new Type(initial);
		}

		public DecalPushConstant create() {
			return new DecalPushConstant(this, initial);
		}

		public DecalPushConstant getFallback() {
			return fallback;
		}
	}

}
