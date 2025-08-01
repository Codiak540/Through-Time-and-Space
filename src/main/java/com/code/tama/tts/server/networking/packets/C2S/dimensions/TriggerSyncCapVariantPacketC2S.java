/* (C) TAMA Studios 2025 */
package com.code.tama.tts.server.networking.packets.C2S.dimensions;

import com.code.tama.tts.Exteriors;
import com.code.tama.tts.server.capabilities.CapabilityConstants;
import com.code.tama.tts.server.networking.Networking;
import com.code.tama.tts.server.networking.packets.S2C.dimensions.SyncCapVariantPacketS2C;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Tells the server to sync the exterior variant with the client level
 * capability
 */
public class TriggerSyncCapVariantPacketC2S {
    public static TriggerSyncCapVariantPacketC2S decode(FriendlyByteBuf buffer) {
        return new TriggerSyncCapVariantPacketC2S(buffer.readResourceKey(Registries.DIMENSION));
    }

    public static void encode(TriggerSyncCapVariantPacketC2S packet, FriendlyByteBuf buffer) {
        buffer.writeResourceKey(packet.TARDISLevel);
    }

    public static void handle(TriggerSyncCapVariantPacketC2S packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ServerLifecycleHooks.getCurrentServer()
                .getLevel(packet.TARDISLevel)
                .getCapability(CapabilityConstants.TARDIS_LEVEL_CAPABILITY)
                .ifPresent(cap -> Networking.sendPacketToDimension(
                        packet.TARDISLevel,
                        new SyncCapVariantPacketS2C(Exteriors.GetOrdinal(cap.GetExteriorVariant())))));

        context.setPacketHandled(true);
    }

    ResourceKey<Level> TARDISLevel;

    public TriggerSyncCapVariantPacketC2S(ResourceKey<Level> TARDISLevel) {
        this.TARDISLevel = TARDISLevel;
    }
}
