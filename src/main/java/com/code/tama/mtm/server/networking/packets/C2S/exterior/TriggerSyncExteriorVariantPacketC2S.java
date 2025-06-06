package com.code.tama.mtm.server.networking.packets.C2S.exterior;

import com.code.tama.mtm.Exteriors;
import com.code.tama.mtm.server.networking.Networking;
import com.code.tama.mtm.server.networking.packets.S2C.exterior.SyncExteriorVariantPacketS2C;
import com.code.tama.mtm.server.tileentities.ExteriorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class TriggerSyncExteriorVariantPacketC2S {
    private final int blockX, blockY, blockZ; // Block position
    private final ResourceKey<Level> level;

    public TriggerSyncExteriorVariantPacketC2S(ResourceKey<Level> level, int x, int y, int z) {
        this.level = level;
        this.blockX = x;
        this.blockY = y;
        this.blockZ = z;
    }

    public static void encode(TriggerSyncExteriorVariantPacketC2S packet, FriendlyByteBuf buffer) {
        buffer.writeResourceKey(packet.level);
        buffer.writeInt(packet.blockX);
        buffer.writeInt(packet.blockY);
        buffer.writeInt(packet.blockZ);
    }

    public static TriggerSyncExteriorVariantPacketC2S decode(FriendlyByteBuf buffer) {
        return new TriggerSyncExteriorVariantPacketC2S(
                buffer.readResourceKey(Registries.DIMENSION),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt()
        );
    }

    public static void handle(TriggerSyncExteriorVariantPacketC2S packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            BlockEntity be = ServerLifecycleHooks.getCurrentServer().getLevel(packet.level).getBlockEntity(
                    new BlockPos(packet.blockX, packet.blockY, packet.blockZ)
            );
            if(be instanceof ExteriorTile exteriorTile) {
                Networking.sendPacketToDimension(
                        packet.level,
                        new SyncExteriorVariantPacketS2C(
                                exteriorTile.getModelIndex(),
                                Exteriors.GetOrdinal(exteriorTile.GetVariant()),
                                packet.blockX,
                                packet.blockY,
                                packet.blockZ
                        )
                );
            }
        });
        context.setPacketHandled(true);
    }
}