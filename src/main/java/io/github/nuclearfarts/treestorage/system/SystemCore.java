package io.github.nuclearfarts.treestorage.system;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import io.github.nuclearfarts.treestorage.block.entity.TreeControllerBlockEntity;
import io.github.nuclearfarts.treestorage.duck.WorldGraphAccessor;
import io.github.nuclearfarts.treestorage.graph.WorldGraphHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SystemCore {
	private final BlockPos pos;
	private final World world;
	private final Collection<SystemComponent> components = new HashSet<>();
	private final Collection<TickingSystemComponent> tickingComponents = new HashSet<>();
	private boolean overloaded = false;
	private int powerCapacity = 0;
	private long storageCapacity = 0;
	
	public SystemCore(BlockPos pos, World world) {
		this.pos = pos;
		this.world = world;
	}
	
	public static SystemCore getForPos(BlockPos pos, World world) {
		WorldGraphHandler gh = ((WorldGraphAccessor) world).treestorage$getGraphHandler();
		BlockPos cPos = gh.getControllerFor(pos);
		if(cPos != null) {
			BlockEntity be = world.getBlockEntity(pos);
			if(be instanceof TreeControllerBlockEntity) {
				TreeControllerBlockEntity cbe = (TreeControllerBlockEntity) be;
				return cbe.getCore();
			}
		}
		return null;
	}
	
	public void tick() {
		Iterator<SystemComponent> iter = components.iterator();
		int powerLoad = 0;
		while(iter.hasNext()) {
			powerLoad += iter.next().getPowerLoad();
		}
		overloaded = powerLoad > powerCapacity;
		if(!overloaded) {
			for(TickingSystemComponent c : tickingComponents) {
				c.tickComponent();
			}
		}
	}
	
	public void addComponent(SystemComponent component) {
		components.add(component);
		if(component.componentTicks()) {
			tickingComponents.add((TickingSystemComponent) component);
		}
	}
	
	public void removeComponent(SystemComponent component) {
		components.remove(component);
		tickingComponents.remove(component);
	}
	
	public boolean isOverloaded() {
		return overloaded;
	}
}
