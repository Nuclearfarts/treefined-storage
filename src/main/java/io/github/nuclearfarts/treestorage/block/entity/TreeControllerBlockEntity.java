package io.github.nuclearfarts.treestorage.block.entity;

import io.github.nuclearfarts.treestorage.TreeStorage;
import io.github.nuclearfarts.treestorage.system.SystemCore;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TreeControllerBlockEntity extends BlockEntity {
	private SystemCore core;
	
	public TreeControllerBlockEntity() {
		super(TreeStorage.CONTROLLER_BE);
	}
	
	public void setLocation(World world, BlockPos pos) {
		super.setLocation(world, pos);
		core = new SystemCore(pos, world);
	}
	
	public SystemCore getCore() {
		return core;
	}
}
