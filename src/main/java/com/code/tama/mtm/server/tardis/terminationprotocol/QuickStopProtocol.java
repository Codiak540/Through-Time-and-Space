package com.code.tama.mtm.server.tardis.terminationprotocol;

import com.code.tama.mtm.server.capabilities.interfaces.ITARDISLevel;
import com.code.tama.mtm.client.CameraShakeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class QuickStopProtocol extends TerminationProtocol {
    public QuickStopProtocol() {}

    @Override
    public void OnLand(ITARDISLevel itardisLevel, BlockPos blockPos, Level level) {
        CameraShakeHandler.startShake(20, 40);
        this.SetLandPos(level.getBlockRandomPos(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 25));
    }
}