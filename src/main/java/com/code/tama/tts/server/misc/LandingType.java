/* (C) TAMA Studios 2025 */
package com.code.tama.tts.server.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public abstract class LandingType {
    public abstract BlockPos GetLandingPos(SpaceTimeCoordinate CurrentLandingPos, ServerLevel level);
}
