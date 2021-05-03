package io.github.nuclearfarts.treestorage.graph;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.nuclearfarts.treestorage.block.system.GraphNodeBlock;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

final class Node {
	private Node core = null;
	private boolean coreDirty = true;
	private BlockPos corePos;
	private final BlockPos pos;
	private final World world;
	private final boolean isCore;
	private final Set<Node> connections = new HashSet<>();
	private final Map<Direction, Set<Node>> connectionsMap = new EnumMap<>(Direction.class);
	
	Node(BlockPos pos, World world, boolean isCore) {
		this.pos = pos;
		this.isCore = isCore;
		this.world = world;
	}
	
	private Node(BlockPos pos, World world, boolean isCore, BlockPos corePos) {
		this(pos, world, isCore);
		this.corePos = corePos;
	}
	
	public void remove() {
		Set<Node> dirty = new HashSet<>();
		for(Node n : connections) {
			n.removeConnection(this, dirty);
		}
		for(Node n : dirty) {
			n.calcCore();
		}
	}
	
	public boolean isCore() {
		return isCore;
	}
	
	public Node getCore() {
		if(coreDirty) {
			calcCore();
		}
		return core;
	}
	
	public void markCoreDirty(Set<Node> marked) {
		if(marked.add(this)) {
			coreDirty = true;
			for(Node n : connections) {
				n.markCoreDirty(marked);
			}
		}
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public void putSideConnections(Direction side, Set<Node> connect, Set<Node> markedDirty) {
		Set<Node> copy = new HashSet<>(connect);
		copy.remove(this);
		Set<Node> prev = connectionsMap.put(side, copy);
		if(prev != null) {
			connections.clear();
			for(Set<Node> nodes : connectionsMap.values()) {
				connections.addAll(nodes);
			}
		} else {
			connections.addAll(copy);
		}
		markCoreDirty(markedDirty);
	}
	
	/**
	 * Calculates the connected core for this node and all connected nodes (since we already did the hard part and they need to know too)
	 */
	public void calcCore() {
		if(coreDirty) {
			Set<Node> found = new HashSet<>();
			Set<Node> searched = new HashSet<>();
			searched.add(this);
			searchCore(searched, found);
			Node core;
			if(found.size() != 1) {
				core = null;
			} else {
				core = found.iterator().next();
			}
			for(Node n : searched) {
				n.setCore(core);
			}
		}
	}
	
	private void searchCore(Set<Node> searched, Set<Node> found) {
		for(Node n : connections) {
			if(searched.add(n)) {
				n.searchCore(searched, found);
			}
		}
		if(isCore) {
			found.add(this);
		}
	}
	
	private void removeConnection(Node n, Set<Node> markedDirty) {
		connections.remove(n);
		for(Set<Node> s : connectionsMap.values()) {
			s.remove(n);
		}
		markCoreDirty(markedDirty);
	}
	
	private void setCore(Node to) {
		core = to;
		corePos = to != null ? to.getPos() : null;
		coreDirty = false;
		if(corePos != null) {
			BlockState state = world.getBlockState(pos);
			((GraphNodeBlock) state.getBlock()).setCore(state, pos, world, corePos);
		}
	}
	
	@Override
	public int hashCode() {
		return pos.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Node)) return false;
		return pos.equals(((Node) other).pos);
	}
	
	public CompoundTag toNbt() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("isC", isCore);
		tag.putLong("pos", pos.asLong());
		if(core != null) {
			tag.putLong("cpos", core.pos.asLong());
		}
		return tag;
	}
	
	public static Node fromNbt(CompoundTag tag, World world) {
		boolean core = tag.getBoolean("isC");
		BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));
		BlockPos corePos = null;
		if(tag.contains("cpos", NbtType.LONG)) {
			corePos = BlockPos.fromLong(tag.getLong("cpos"));
		}
		return new Node(pos, world, core, corePos);
	}
	
	public void loadCoreFromPos(Map<BlockPos, Node> map) {
		if(corePos != null) {
			core = map.get(corePos);
			coreDirty = false;
		}
	}
}