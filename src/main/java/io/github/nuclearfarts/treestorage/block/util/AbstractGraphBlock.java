package io.github.nuclearfarts.treestorage.block.util;

import io.github.nuclearfarts.treestorage.block.system.GraphBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractGraphBlock extends Block implements GraphBlock {
	public AbstractGraphBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		GraphBlock.super.onBlockAdded(state, world, pos, oldState, notify);
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		GraphBlock.super.onStateReplaced(state, world, pos, newState, moved);
	}
}
