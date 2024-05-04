package net.darktree.rust;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rust implements ModInitializer {

	public static final String NAMESPACE = "rust";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Item.Settings ITEM_SETTINGS = new Item.Settings();

	public static Identifier id(String name) {
		return new Identifier(NAMESPACE, name);
	}

	public static final Block TEST = new Block(AbstractBlock.Settings.create().dropsNothing().solid());
	public static final BlockItem TESTI = new BlockItem(TEST, ITEM_SETTINGS);

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, id("test"), TEST);
		Registry.register(Registries.ITEM, id("test"), TESTI);
	}

}