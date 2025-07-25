/* (C) TAMA Studios 2025 */
package com.code.tama.tts.server.tardis.terminationprotocol;

import com.code.tama.tts.client.util.CameraShakeHandler;
import com.code.tama.tts.server.capabilities.interfaces.ITARDISLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class EmergencyStopProtocol extends TerminationProtocol {
    public EmergencyStopProtocol() {}

    @Override
    public void OnLand(ITARDISLevel itardisLevel, BlockPos blockPos, Level level) {
        // 50/50 chance of crashing
        if (level.getRandom().nextInt(2) > 1) itardisLevel.Crash();

        CameraShakeHandler.startShake(20, 40);
        this.SetLandPos(level.getBlockRandomPos(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 50));
    }
}
