package net.darktree.rust.network;

import io.netty.buffer.Unpooled;
import net.darktree.rust.Rust;
import net.darktree.rust.util.duck.PlayerRotationView;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AssemblyRotationC2SPacket implements PacketConsumer {

	public static final Identifier ID = Rust.id("assembly_rotation");

	public void register() {
		ServerSidePacketRegistry.INSTANCE.register(ID, this);
	}

	public void accept(PacketContext context, PacketByteBuf buffer) {
		BlockRotation rotation = buffer.readEnumConstant(BlockRotation.class);
		context.getTaskQueue().execute(() -> apply((ServerPlayerEntity) context.getPlayer(), rotation));
	}

	private void apply(ServerPlayerEntity player, BlockRotation rotation) {
		PlayerRotationView.of(player).rust_setAssemblyRotation(rotation);
	}

	public void send(BlockRotation rotation, Consumer<PacketByteBuf> sender) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeEnumConstant(rotation);
		sender.accept(data);
	}

}
