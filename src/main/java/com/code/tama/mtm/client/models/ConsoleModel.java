package com.code.tama.mtm.client.models;

import com.code.tama.mtm.server.tileentities.ConsoleTile;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class ConsoleModel<T extends BlockEntity> extends HierarchicalModel<Entity> {

    private final ModelPart Root;

    public ConsoleModel(ModelPart root){
        this(root, RenderType::entityCutoutNoCull);
    }

    public ConsoleModel(ModelPart root, Function<ResourceLocation, RenderType> renderTypeFunction){
        super(renderTypeFunction);
        this.Root = root;
    }

    public abstract void SetupAnimations(@NotNull ConsoleTile tile, float ageInTicks);

    @Override
    public @NotNull ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(@NotNull Entity E, float LimbSwing, float LimbSwingAmount, float AgeInTicks, float NetHeadYaw, float HeadPitch) {}
}

