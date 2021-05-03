package io.github.nuclearfarts.treestorage.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.nuclearfarts.treestorage.duck.WorldGraphAccessor;
import io.github.nuclearfarts.treestorage.graph.WorldGraphHandler;
import net.minecraft.world.World;

@Mixin(World.class)
public class WorldMixin implements WorldGraphAccessor {
	@Unique private @Final WorldGraphHandler graphHandler;
	
	@Inject(at = @At("TAIL"), method = "<init>")
	private void initGraphHandler(CallbackInfo info) {
		graphHandler = new WorldGraphHandler((World) (Object) this);
	}

	@Override
	public WorldGraphHandler treestorage$getGraphHandler() {
		return graphHandler;
	}
}
