package net.darktree.rust.network;

public class RustPackets {

	public static AssemblyRotationC2SPacket ROTATION = new AssemblyRotationC2SPacket();

	public static void init() {
		ROTATION.register();
	}

}
