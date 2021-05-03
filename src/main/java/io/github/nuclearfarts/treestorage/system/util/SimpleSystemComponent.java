package io.github.nuclearfarts.treestorage.system.util;

import io.github.nuclearfarts.treestorage.system.SystemComponent;
import io.github.nuclearfarts.treestorage.system.SystemCore;

public class SimpleSystemComponent implements SystemComponent {
	protected SystemCore core = null;
	private final int powerLoad;
	
	public SimpleSystemComponent(int powerLoad) {
		this.powerLoad = powerLoad;
	}
	
	@Override
	public void setCore(SystemCore newCore) {
		if(core != null) {
			core.removeComponent(this);
		}
		if(newCore != null) {
			newCore.addComponent(this);
		}
		core = newCore;
	}

	@Override
	public int getPowerLoad() {
		return powerLoad;
	}
	
	public void destroy() {
		if(core != null) {
			core.removeComponent(this);
		}
	}
}
