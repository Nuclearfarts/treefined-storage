package io.github.nuclearfarts.treestorage.block.util;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class Connect6Block extends ConnectingBlock implements Waterloggable {
	public Connect6Block(float radius, Settings settings) {
		super(radius, settings);
		BlockState defaultState = getDefaultState().with(Properties.WATERLOGGED, false);
		for(BooleanProperty p : FACING_PROPERTIES.values()) {
			defaultState = defaultState.with(p, false);
		}
		setDefaultState(defaultState);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if(state.get(Properties.WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return state.with(FACING_PROPERTIES.get(direction), shouldConnect(state, pos, direction, newState, posFrom, world));
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, Properties.WATERLOGGED);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		World wld = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		BlockState state = getDefaultState();
		for(Map.Entry<Direction, BooleanProperty> e : FACING_PROPERTIES.entrySet()) {
			BlockPos checkPos = pos.offset(e.getKey());
			if(shouldConnect(state, pos, e.getKey(), wld.getBlockState(checkPos), checkPos, wld)) {
				state = state.with(e.getValue(), true);
			}
		}
		if(wld.getFluidState(pos).getFluid() == Fluids.WATER) {
			state = state.with(Properties.WATERLOGGED, true);
		}
		return state;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	protected abstract boolean shouldConnect(BlockState self, BlockPos pos, Direction side, BlockState other, BlockPos otherPos, WorldAccess world);
}
