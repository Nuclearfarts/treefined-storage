package io.github.nuclearfarts.treestorage.block.system;

import java.util.Set;

import io.github.nuclearfarts.treestorage.block.util.AbstractGraphBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GraphTestBlock extends AbstractGraphBlock implements GraphNodeBlock {
	public GraphTestBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public Set<Direction> getConnectedDirectionsUncached(BlockState state) {
		return ALL_DIRECTIONS;
	}
	
	@Override
	public boolean isNode(BlockState state) {
		return true;
	}

	@Override
	public void setCore(BlockState state, BlockPos myPos, World world, BlockPos newCore) {
		System.out.println("Core for " + myPos + " set to " + newCore);
	}
}
