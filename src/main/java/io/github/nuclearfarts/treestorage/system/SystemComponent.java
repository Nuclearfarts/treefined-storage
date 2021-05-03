package io.github.nuclearfarts.treestorage.system;

public interface SystemComponent {
	int getPowerLoad();
	/**
	 * Responsible for adding this component to the new core and removing it from the old one (if any)
	 */
	void setCore(SystemCore core);
	
	default boolean componentTicks() {
		return false;
	}
}
