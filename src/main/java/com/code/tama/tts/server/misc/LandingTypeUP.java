/* (C) TAMA Studios 2025 */
package com.code.tama.tts.server.misc;

import com.code.tama.triggerapi.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class LandingTypeUP extends LandingType {

    @Override
    public BlockPos GetLandingPos(SpaceTimeCoordinate CurrentLandingPos, ServerLevel level) {
        return CurrentLandingPos.GetBlockPos()
                .atY(WorldHelper.SafeBottomY(level, CurrentLandingPos.GetBlockPos()) + 1)
                .offset(
                        0,
                        WorldHelper.getSurfaceHeight(
                                level, ((int) CurrentLandingPos.GetX()), ((int) CurrentLandingPos.GetZ())),
                        0);
    }
}
