package io.github.nuclearfarts.treestorage.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import io.github.nuclearfarts.treestorage.block.system.GraphBlock;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WorldGraphHandler {
	private final Map<BlockPos, Node> nodes = new HashMap<>();
	private final World world;
	
	public WorldGraphHandler(World world) {
		this.world = world;
	}
	
	public void addNodeAt(BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		GraphBlock g = (GraphBlock) state.getBlock();
		if(!g.isNode(state)) {
			throw new IllegalArgumentException("Block at " + pos + " is not a node");
		}
		Node node = new Node(pos, world, g.isCore(state));
		nodes.put(pos, node);
		for(Direction d : g.getConnectedDirections(state)) {
			BlockPos sPos = pos.offset(d);
			searchAt(sPos, d);
		}
	}
	
	public void addNonNodeAt(BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		GraphBlock g = (GraphBlock) state.getBlock();
		if(g.isNode(state)) {
			throw new IllegalArgumentException("Block at " + pos + " is a node");
		}
		searchAt(pos, null);
	}
	
	public void searchAt(BlockPos pos, Direction from) {
		Set<Pair<BlockPos, Direction>> results = new HashSet<>();
		searchAt(pos, results, new HashSet<>(), from);
		Set<Node> foundNodes = new HashSet<>();
		for(Pair<BlockPos, Direction> p : results) {
			Node n = nodes.get(p.getLeft());
			if(n != null) {
				foundNodes.add(n);
			}
		}
		Set<Node> batch = new HashSet<>();
		for(Pair<BlockPos, Direction> p : results) {
			Node n = nodes.get(p.getLeft());
			if(n != null) {
				n.putSideConnections(p.getRight(), foundNodes, batch);
			}
		}
		
		for(Node n : batch) {
			n.calcCore();
		}
	}
	
	private void searchAt(BlockPos pos, Collection<Pair<BlockPos, Direction>> result, Set<BlockPos> searched, Direction from) {
		if(!searched.add(pos)) {
			return;
		}
		BlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		if(b instanceof GraphBlock) {
			GraphBlock g = (GraphBlock) b;
			if(from == null || g.getConnectedDirections(state).contains(from.getOpposite())) {
				if(g.isNode(state)) {
					result.add(Pair.of(pos, from.getOpposite()));
				} else {
					for(Direction d : g.getConnectedDirections(state)) {
						searchAt(pos.offset(d), result, searched, d);
					}
				}
			}
		}
	}
	
	public void removeNodeAt(BlockPos pos) {
		Node n = nodes.remove(pos);
		if(n != null) {
			n.remove();
		}
	}
	
	public void removeNonNodeAt(BlockPos pos, BlockState removed) {
		GraphBlock g = (GraphBlock) removed.getBlock();
		for(Direction d : g.getConnectedDirections(removed)) {
			searchAt(pos.offset(d), d);
		}
	}
	
	public boolean hasNodeAt(BlockPos pos) {
		return nodes.containsKey(pos);
	}
	
	public @Nullable BlockPos getControllerFor(BlockPos pos) {
		// the null propagation at home
		Node n = nodes.get(pos);
		if(n != null) {
			Node core = n.getCore();
			if(core != null) {
				return core.getPos();
			}
		}
		return null;
	}
	
	public CompoundTag toNbt() {
		CompoundTag tag = new CompoundTag();
		ListTag nodeList = new ListTag();
		for(Node n : nodes.values()) {
			nodeList.add(n.toNbt());
		}
		tag.put("Nodes", nodeList);
		return tag;
	}
	
	public static WorldGraphHandler fromNbt(World world, CompoundTag tag) {
		WorldGraphHandler gh = new WorldGraphHandler(world);
		ListTag nodeList = tag.getList("Nodes", NbtType.COMPOUND);
		for(Tag t : nodeList) {
			Node n = Node.fromNbt((CompoundTag) t, world);
			gh.nodes.put(n.getPos(), n);
		}
		for(Node n : gh.nodes.values()) {
			n.loadCoreFromPos(gh.nodes);
		}
		return gh;
	}
}
