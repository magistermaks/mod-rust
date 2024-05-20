package net.darktree.rust;

import net.darktree.rust.assembly.AssemblyType;
import net.darktree.rust.assembly.decal.DecalPushConstant;
import net.darktree.rust.assembly.decal.DecalType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class RustRegistries {

	public static class Key {
		public static final RegistryKey<Registry<AssemblyType>> ASSEMBLY = RegistryKey.ofRegistry(Rust.id("assembly"));
		public static final RegistryKey<Registry<DecalPushConstant.Type>> CONSTANT = RegistryKey.ofRegistry(Rust.id("constant"));
		public static final RegistryKey<Registry<DecalType<?, ?>>> DECAL = RegistryKey.ofRegistry(Rust.id("decal"));
	}

	public static final Registry<AssemblyType> ASSEMBLY = FabricRegistryBuilder.createSimple(Key.ASSEMBLY).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<DecalPushConstant.Type> CONSTANT = FabricRegistryBuilder.createSimple(Key.CONSTANT).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<DecalType<?, ?>> DECAL = FabricRegistryBuilder.createSimple(Key.DECAL).buildAndRegister();

}
