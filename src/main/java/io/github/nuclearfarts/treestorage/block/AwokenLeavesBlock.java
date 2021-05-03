package io.github.nuclearfarts.treestorage.block;

import io.github.nuclearfarts.treestorage.block.util.BlockWithColorProvider;
import io.github.nuclearfarts.treestorage.block.util.BlockWithItemColorProvider;
import io.github.nuclearfarts.treestorage.block.util.BlockWithRenderLayer;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.render.RenderLayer;

public class AwokenLeavesBlock extends Block implements BlockWithColorProvider, BlockWithItemColorProvider, BlockWithRenderLayer {
	private final int color;
	public AwokenLeavesBlock(Settings settings, int color) {
		super(settings);
		this.color = color;
	}

	@Override
	public BlockColorProvider getColorProvider() {
		return (s, w, p, i) -> i == 0 ? color : -1;
	}

	@Override
	public RenderLayer getBlockRenderLayer() {
		return RenderLayer.getCutoutMipped();
	}

	@Override
	public ItemColorProvider getItemColorProvider() {
		return (a, i) -> i == 0 ? color : -1;
	}
}
