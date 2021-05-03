package io.github.nuclearfarts.treestorage.block;

import io.github.nuclearfarts.treestorage.block.util.BlockWithRenderLayer;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.render.RenderLayer;

public class AwokenLogBlock extends PillarBlock implements BlockWithRenderLayer {
	public AwokenLogBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public RenderLayer getBlockRenderLayer() {
		return RenderLayer.getCutout();
	}
}
