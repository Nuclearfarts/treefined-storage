package io.github.nuclearfarts.treestorage.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;

import io.github.nuclearfarts.treestorage.block.system.GraphBlock;
import io.github.nuclearfarts.treestorage.duck.BlockStateExt;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;

@Mixin(BlockState.class)
public abstract class BlockStateMixin extends AbstractBlock.AbstractBlockState implements BlockStateExt {
	@Unique private Set<Direction> connectionDirections;
	protected BlockStateMixin(Block block, ImmutableMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> mapCodec) {
		super(block, propertyMap, mapCodec);
		throw new Error("Mixin constructor called");
	}

	@Override
	public Set<Direction> treestorage$getConnectionDirectionSetCached() {
		if(connectionDirections == null) {
			Block bl = getBlock();
			if(bl instanceof GraphBlock) {
				GraphBlock gb = (GraphBlock) bl;
				connectionDirections = gb.getConnectedDirectionsUncached((BlockState) (Object) this);
			} else {
				throw new IllegalArgumentException("Attempted to get connection direction set for non-graphblock");
			}
		}
		return connectionDirections;
	}
}
