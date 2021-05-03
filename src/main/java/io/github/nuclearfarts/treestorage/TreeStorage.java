package io.github.nuclearfarts.treestorage;

import java.util.Collections;

import io.github.nuclearfarts.mcap.TemplateType;
import io.github.nuclearfarts.mcap.annotation.BlockRegistryCallback;
import io.github.nuclearfarts.mcap.annotation.FieldRef;
import io.github.nuclearfarts.mcap.annotation.ItemRegistryCallback;
import io.github.nuclearfarts.mcap.annotation.RegisterBlock;
import io.github.nuclearfarts.mcap.annotation.RegistryContainer;
import io.github.nuclearfarts.mcap.annotation.Template;
import io.github.nuclearfarts.treestorage.block.AwokenLeavesBlock;
import io.github.nuclearfarts.treestorage.block.util.BlockWithColorProvider;
import io.github.nuclearfarts.treestorage.block.util.BlockWithItemColorProvider;
import io.github.nuclearfarts.treestorage.block.util.BlockWithRenderLayer;
import io.github.nuclearfarts.treestorage.block.AwokenLogBlock;
import io.github.nuclearfarts.treestorage.block.entity.TreeControllerBlockEntity;
import io.github.nuclearfarts.treestorage.block.system.AwokenRootBlock;
import io.github.nuclearfarts.treestorage.block.system.GraphTestBlock;
import io.github.nuclearfarts.treestorage.block.system.TreeController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@RegistryContainer(
		value = "treestorage",
		block = "basic",
		blockState = "basic",
		autoBlockItem = true,
		blockItem = "block",
		item = "basic",
		loot = "basic",
		itemGroup = @FieldRef(clazz = TreeStorage.class, field = "TREE_STORAGE_ITEM_GROUP"),
		templates = {
				@Template(name = "inherit", file = "block/inherit.json", type = TemplateType.BLOCK),
				@Template(name = "awoken_log", file = "block/awoken_log.json", type = TemplateType.BLOCK),
				@Template(name = "awoken_log_h", file = "block/awoken_log_horizontal.json", type = TemplateType.BLOCK),
				@Template(name = "column", file = "blockstate/column.json", type = TemplateType.BLOCKSTATE),
				@Template(name = "connect6", file = "blockstate/connect6.json", type = TemplateType.BLOCKSTATE)
		}
)
public class TreeStorage implements ModInitializer {
	public static final ItemGroup TREE_STORAGE_ITEM_GROUP = FabricItemGroupBuilder.build(id("itemgroup"), () -> new ItemStack(Items.OAK_SAPLING));
	
	@RegisterBlock(value = "awoken_oak_log", model = "awoken_log", modelArgs = "oak", blockState = "column")
	public static final Block AWOKEN_OAK_LOG = new AwokenLogBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
	@RegisterBlock(value = "awoken_birch_log", model = "awoken_log_h", modelArgs = "birch", blockState = "column")
	public static final Block AWOKEN_BIRCH_LOG = new AwokenLogBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LOG));
	@RegisterBlock(value = "awoken_spruce_log", model = "awoken_log", modelArgs = "spruce", blockState = "column")
	public static final Block AWOKEN_SPRUCE_LOG = new AwokenLogBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_LOG));
	@RegisterBlock(value = "awoken_jungle_log", model = "awoken_log_h", modelArgs = "jungle", blockState = "column")
	public static final Block AWOKEN_JUNGLE_LOG = new AwokenLogBlock(FabricBlockSettings.copyOf(Blocks.JUNGLE_LOG));
	@RegisterBlock(value = "awoken_dark_oak_log", model = "awoken_log", modelArgs = "dark_oak", blockState = "column")
	public static final Block AWOKEN_DARK_OAK_LOG = new AwokenLogBlock(FabricBlockSettings.copyOf(Blocks.DARK_OAK_LOG));
	@RegisterBlock(value = "awoken_acacia_log", model = "awoken_log", modelArgs = "acacia", blockState = "column")
	public static final Block AWOKEN_ACACIA_LOG = new AwokenLogBlock(FabricBlockSettings.copyOf(Blocks.ACACIA_LOG));
	
	@RegisterBlock(value = "awoken_oak_leaves", model = "inherit", modelArgs = "minecraft:block/oak_leaves")
	public static final Block AWOKEN_OAK_LEAVES = new AwokenLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES), 0xFF0FCE0F);
	@RegisterBlock(value = "awoken_birch_leaves", model = "inherit", modelArgs = "minecraft:block/birch_leaves")
	public static final Block AWOKEN_BIRCH_LEAVES = new AwokenLeavesBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LEAVES), 0xFF0FCE3F);
	@RegisterBlock(value = "awoken_spruce_leaves", model = "inherit", modelArgs = "minecraft:block/spruce_leaves")
	public static final Block AWOKEN_SPRUCE_LEAVES = new AwokenLeavesBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_LEAVES), 0xFF0FCE0F);
	@RegisterBlock(value = "awoken_jungle_leaves", model = "inherit", modelArgs = "minecraft:block/jungle_leaves")
	public static final Block AWOKEN_JUNGLE_LEAVES = new AwokenLeavesBlock(FabricBlockSettings.copyOf(Blocks.JUNGLE_LEAVES), 0xFF0FCE0F);
	@RegisterBlock(value = "awoken_dark_oak_leaves", model = "inherit", modelArgs = "minecraft:block/dark_oak_leaves")
	public static final Block AWOKEN_DARK_OAK_LEAVES = new AwokenLeavesBlock(FabricBlockSettings.copyOf(Blocks.DARK_OAK_LEAVES), 0xFF097209);
	@RegisterBlock(value = "awoken_acacia_leaves", model = "inherit", modelArgs = "minecraft:block/acacia_leaves")
	public static final Block AWOKEN_ACACIA_LEAVES = new AwokenLeavesBlock(FabricBlockSettings.copyOf(Blocks.ACACIA_LEAVES), 0xFF5BCE0F);
	
	@RegisterBlock("tree_controller")
	public static final Block TREE_CONTROLLER = new TreeController(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
	@RegisterBlock("graph_test")
	public static final Block GRAPH_TEST_BLOCK = new GraphTestBlock(FabricBlockSettings.copy(Blocks.STONE));
	
	@RegisterBlock(value = "awoken_root", model = "", blockState = "connect6")
	public static final Block AWOKEN_ROOT = new AwokenRootBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)); 
	
	public static final BlockEntityType<TreeControllerBlockEntity> CONTROLLER_BE = new BlockEntityType<>(TreeControllerBlockEntity::new, Collections.singleton(TREE_CONTROLLER), null);
	
	@BlockRegistryCallback
	static void registerBlock(Identifier id, Block block) {
		if(block instanceof BlockWithRenderLayer) {
			BlockRenderLayerMap.INSTANCE.putBlock(block, ((BlockWithRenderLayer) block).getBlockRenderLayer());
		}
		if(block instanceof BlockWithColorProvider) {
			ColorProviderRegistry.BLOCK.register(((BlockWithColorProvider) block).getColorProvider(), block);
		}
		Registry.register(Registry.BLOCK, id, block);
	}
	
	@ItemRegistryCallback
	static void registerItem(Identifier id, Item item) {
		if(item instanceof BlockItem) {
			Block b = ((BlockItem) item).getBlock();
			if(b instanceof BlockWithItemColorProvider) {
				ColorProviderRegistry.ITEM.register(((BlockWithItemColorProvider) b).getItemColorProvider(), item);
			}
		}
		Registry.register(Registry.ITEM, id, item);
	}
	
	@Override
	public void onInitialize() {
		TreeStorageRegistrar.register();
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("tree_controller"), CONTROLLER_BE);
	}
	
	public static Identifier id(String s) {
		return new Identifier("treestorage", s);
	}
}
