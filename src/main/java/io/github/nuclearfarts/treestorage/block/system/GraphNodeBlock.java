package io.github.nuclearfarts.treestorage.block.system;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface GraphNodeBlock extends GraphBlock {
	void setCore(BlockState state, BlockPos myPos, World world, BlockPos newCore);
	
	@Override
	default boolean isNode(BlockState state) {
		return true;
	}
}
