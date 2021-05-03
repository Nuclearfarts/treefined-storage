package io.github.nuclearfarts.treestorage.system.util;

import io.github.nuclearfarts.treestorage.system.TickingSystemComponent;

public class SimpleTickingSystemComponent extends SimpleSystemComponent implements TickingSystemComponent {
	private final Runnable tick;
	
	public SimpleTickingSystemComponent(int powerLoad, Runnable tick) {
		super(powerLoad);
		this.tick = tick;
	}
	
	@Override
	public void tickComponent() {
		tick.run();
	}
}
