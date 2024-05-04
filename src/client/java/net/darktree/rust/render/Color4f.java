package net.darktree.rust.render;

public class Color4f {

	public float r;
	public float g;
	public float b;
	public float a;

	public Color4f(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Color4f(float v) {
		this(v, v, v, v);
	}

	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

}
