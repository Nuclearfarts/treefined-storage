package io.github.nuclearfarts.treestorage.block.system;

import java.util.EnumSet;
import java.util.Set;

import org.jetbrains.annotations.ApiStatus;

import io.github.nuclearfarts.treestorage.duck.BlockStateExt;
import io.github.nuclearfarts.treestorage.duck.WorldGraphAccessor;
import io.github.nuclearfarts.treestorage.graph.WorldGraphHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface GraphBlock {
	static final Set<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);
	
	/**
	 * use {@link #getConnectedDirections(BlockState) getConnectedDirections} instead
	 */
	@ApiStatus.OverrideOnly
	Set<Direction> getConnectedDirectionsUncached(BlockState state);
	
	/**
	 * Gets whether or not this is a proper node of the graph, or merely something that carries a signal along.
	 */
	default boolean isNode(BlockState state) {
		return false;
	}
	
	/**
	 * Gets the directions this blockstate is currently connected to (used primarily for graph searching.)
	 * @param state state of this block
	 * @return directions that this block connects in the given state
	 */
	default Set<Direction> getConnectedDirections(BlockState state) {
		return ((BlockStateExt) state).treestorage$getConnectionDirectionSetCached();
	}
	
	/**
	 * Method for roots to tell how to connect
	 */
	default boolean willConnect(BlockState state, Direction dir) {
		return getConnectedDirections(state).contains(dir);
	}
	
	default boolean isCore(BlockState state) {
		return false;
	}
	
	/**
	 * Call this from onBlockAdded to ensure proper handling.
	 */
	default void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		WorldGraphHandler graph = ((WorldGraphAccessor) world).treestorage$getGraphHandler();
		if(isNode(state)) {
			graph.addNodeAt(pos);
		} else {
			graph.addNonNodeAt(pos);
		}
	}
	
	/**
	 * Call this from onStateReplaced to ensure proper handling.
	 */
	default void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if(!newState.isOf((Block) this)) {
			WorldGraphHandler graph = ((WorldGraphAccessor) world).treestorage$getGraphHandler();
			if(isNode(state)) {
				graph.removeNodeAt(pos);
			} else {
				graph.removeNonNodeAt(pos, state);
			}
		}
	}
}
