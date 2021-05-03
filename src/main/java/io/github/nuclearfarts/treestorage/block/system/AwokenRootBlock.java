package io.github.nuclearfarts.treestorage.block.system;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import io.github.nuclearfarts.treestorage.block.util.Connect6Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class AwokenRootBlock extends Connect6Block implements GraphBlock {
	public AwokenRootBlock(Settings settings) {
		super(0.125F, settings);
	}

	@Override
	public boolean shouldConnect(BlockState self, BlockPos pos, Direction side, BlockState other, BlockPos otherPos, WorldAccess world) {
		if(other.getBlock() instanceof GraphBlock) {
			GraphBlock gb = (GraphBlock) other.getBlock();
			if(gb.willConnect(other, side.getOpposite())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<Direction> getConnectedDirectionsUncached(BlockState state) {
		Set<Direction> connects = EnumSet.noneOf(Direction.class);
		for(Map.Entry<Direction, BooleanProperty> e : FACING_PROPERTIES.entrySet()) {
			if(state.get(e.getValue())) {
				connects.add(e.getKey());
			}
		}
		return connects;
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
	
	@Override
	public boolean willConnect(BlockState state, Direction side) {
		return true;
	}
	
	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return !state.get(Properties.WATERLOGGED);
	}
}
