package io.github.nuclearfarts.treestorage.system;

public interface TickingSystemComponent extends SystemComponent {
	void tickComponent();
	
	@Override
	default boolean componentTicks() {
		return true;
	}
}
