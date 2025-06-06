package com.code.tama.mtm.server.misc;

import com.code.tama.mtm.server.networking.packets.S2C.dimensions.SyncDimensionsS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ClientHelper {
    /** Doesn't work when this code is called from the packet so it gets its own method **/
    public static void handleDimSyncPacket(SyncDimensionsS2C mes){

        if(Minecraft.getInstance().player == null || Minecraft.getInstance().player.connection.levels() == null)
            return;

        Set<ResourceKey<Level>> levels = Minecraft.getInstance().player.connection.levels();
        //If this player knows about this dimension
        if(levels.contains(mes.level)){
            //If remove
            if(!mes.add){
                levels.remove(mes.level);
            }
        }
        //If player does not know about this dim, and we're trying to add it
        else if(mes.add){
            levels.add(mes.level);
        }
    }
}
