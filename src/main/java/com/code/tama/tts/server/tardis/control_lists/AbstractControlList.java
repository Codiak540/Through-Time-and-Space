/* (C) TAMA Studios 2025 */
package com.code.tama.tts.server.tardis.control_lists;

import java.util.HashMap;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractControlList {
    public abstract HashMap<Vec3, Vec3> GetPositionSizeMap();
}
