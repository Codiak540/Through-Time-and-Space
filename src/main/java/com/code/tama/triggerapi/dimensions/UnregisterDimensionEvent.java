/* (C) TAMA Studios 2025 */
package com.code.tama.triggerapi.dimensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fires when a dimension/level is about to be unregistered.<br>
 * This event fires on
 * {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS} and is not
 * cancellable.<br>
 */
public class UnregisterDimensionEvent extends Event {
    private final ServerLevel level;

    public UnregisterDimensionEvent(ServerLevel level) {
        this.level = level;
    }

    /**
     * @return The level that is about to be unregistered.
     */
    public ServerLevel getLevel() {
        return this.level;
    }
}
