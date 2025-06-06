package com.code.tama.mtm.server.tardis.controls;

import com.code.tama.mtm.server.capabilities.interfaces.ITARDISLevel;
import com.code.tama.mtm.client.MTMSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class Y_Control extends AbstractControl {
    @Override
    public InteractionResult OnRightClick(ITARDISLevel itardisLevel, Player player) {
        itardisLevel.SetDestination(
                itardisLevel.GetDestination().AddY(
                        player.isCrouching() ? -itardisLevel.GetIncrement() : itardisLevel.GetIncrement()));
        player.displayClientMessage(Component.literal("Current Destination = " + itardisLevel.GetDestination().ReadableString()), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult OnLeftClick(ITARDISLevel itardisLevel, Entity entity) {
        itardisLevel.SetDestination(
                itardisLevel.GetDestination().AddY(
                        entity.isCrouching() ? itardisLevel.GetIncrement() : -itardisLevel.GetIncrement()));
        if(entity instanceof Player player) player.displayClientMessage(Component.literal("Current Destination = " + itardisLevel.GetDestination().ReadableString()), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public SoundEvent GetSuccessSound() {
        return MTMSounds.BUTTON_CLICK_01.get();
    }

    @Override
    public SoundEvent GetFailSound() {
        return SoundEvents.DISPENSER_FAIL;
    }

    @Override
    public String GetName() {
        return "y_control";
    }
}