/* (C) TAMA Studios 2025 */
package com.code.tama.triggerapi;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryAccess.ImmutableRegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ReflectionBuddy {
    public static <FIELDHOLDER, FIELDTYPE> MutableInstanceField<FIELDHOLDER, FIELDTYPE> getInstanceField(
            Class<FIELDHOLDER> fieldHolderClass, String fieldName) {
        return new MutableInstanceField<>(fieldHolderClass, fieldName);
    }

    /**
     * Gets a getter-like object for a reflective field. Only to be used for
     * obfuscatable vanilla minecraft fields
     *
     * @param <FIELDHOLDER>
     *            The type of the object containing the field
     * @param <FIELDTYPE>
     *            The type of the values the field would contain
     * @param fieldHolderClass
     *            The class of the object containing the field
     * @param fieldName
     *            The SRG (intermediary-obfuscated) name of the field
     * @return A getter for the field
     */
    public static <FIELDHOLDER, FIELDTYPE> Function<FIELDHOLDER, FIELDTYPE> getInstanceFieldGetter(
            Class<FIELDHOLDER> fieldHolderClass, String fieldName) {
        // forge's ORH is needed to reflect into vanilla minecraft java
        Field field = ObfuscationReflectionHelper.findField(fieldHolderClass, fieldName);
        return getInstanceFieldGetter(field);
    }

    @SuppressWarnings("unchecked")
    // throws ClassCastException if the types are wrong, the returned function can
    // also throw RuntimeException
    private static <FIELDHOLDER, FIELDTYPE> Function<FIELDHOLDER, FIELDTYPE> getInstanceFieldGetter(Field field) {
        return instance -> {
            try {
                return (FIELDTYPE) (field.get(instance));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static class BlockAccess {
        public static final Function<Block, Boolean> hasCollision = getInstanceFieldGetter(Block.class, "hasCollision");
    }

    public static class DelegateBorderChangeListenerAccess {
        public static final Function<BorderChangeListener.DelegateBorderChangeListener, WorldBorder> worldBorder =
                getInstanceFieldGetter(BorderChangeListener.DelegateBorderChangeListener.class, "f_61864_");
    }

    public static class EndDragonFightAccess {
        public static final Function<EndDragonFight, Boolean> dragonKilled =
                getInstanceFieldGetter(EndDragonFight.class, "f_64068_");
    }

    public static class Entity {
        public static final Function<net.minecraft.world.entity.Entity, EntityDimensions> dimensions =
                getInstanceFieldGetter(net.minecraft.world.entity.Entity.class, "f_19815_");
    }

    public static class ImmutableRegistryAccessAccess {
        public static final MutableInstanceField<
                        ImmutableRegistryAccess,
                        Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>>>
                registries = getInstanceField(ImmutableRegistryAccess.class, "f_206223_");
    }

    public static class LayeredRegistryAccessAccess {
        @SuppressWarnings("rawtypes")
        public static final Function<LayeredRegistryAccess, RegistryAccess.Frozen> composite =
                getInstanceFieldGetter(LayeredRegistryAccess.class, "f_244063_");

        @SuppressWarnings("rawtypes")
        public static final MutableInstanceField<LayeredRegistryAccess, List<RegistryAccess.Frozen>> values =
                getInstanceField(LayeredRegistryAccess.class, "f_244050_");
    }

    /**
     * fields in MinecraftServer needed for registering dimensions
     **/
    public static class MinecraftServerAccess {
        public static final Function<MinecraftServer, Executor> executor =
                getInstanceFieldGetter(MinecraftServer.class, "f_129738_");
        // we need to read some private fields in MinecraftServer
        // we can use Access Transformers, Accessor Mixins, or
        // ObfuscationReflectionHelper to get at these
        // we'll use ORH here as ATs and Mixins seem to be causing headaches for
        // dependant mods lately
        // it also lets us define the private-field-getting-shenanigans in the same
        // class we're using them
        // it also doesn't need any extra resources or buildscript stuff, which makes
        // this example simpler to describe
        public static final Function<MinecraftServer, ChunkProgressListenerFactory> progressListenerFactory =
                getInstanceFieldGetter(MinecraftServer.class, "f_129756_");
        public static final Function<MinecraftServer, LayeredRegistryAccess<RegistryLayer>> registries =
                getInstanceFieldGetter(MinecraftServer.class, "f_244176_");
        public static final Function<MinecraftServer, LevelStorageAccess> storageSource =
                getInstanceFieldGetter(MinecraftServer.class, "f_129744_");
    }

    public static class ModelPartAccess {
        public static final Function<ModelPart, List<ModelPart.Cube>> cubes =
                getInstanceFieldGetter(ModelPart.class, "f_104212_");
    }

    public static class MutableInstanceField<FIELDHOLDER, FIELDTYPE> {
        // the returned function throws RuntimeException if the types are wrong
        private static <FIELDHOLDER, FIELDTYPE> BiConsumer<FIELDHOLDER, FIELDTYPE> getInstanceFieldSetter(Field field) {
            return (instance, value) -> {
                try {
                    field.set(instance, value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            };
        }

        private final Function<FIELDHOLDER, FIELDTYPE> getter;

        private final BiConsumer<FIELDHOLDER, FIELDTYPE> setter;

        private MutableInstanceField(Class<FIELDHOLDER> fieldHolderClass, String fieldName) {
            Field field = ObfuscationReflectionHelper.findField(fieldHolderClass, fieldName);
            this.getter = getInstanceFieldGetter(field);
            this.setter = getInstanceFieldSetter(field);
        }

        /**
         * Returns the current value of the field in a given instance
         *
         * @param instance
         *            The object containing the instance field to get the value from
         * @return The value in that field
         */
        public FIELDTYPE get(FIELDHOLDER instance) {
            return this.getter.apply(instance);
        }

        /**
         * Sets an object's field to the given value
         *
         * @param instance
         *            The object containing the instance field to set the value in
         * @param value
         *            The value to set
         */
        public void set(FIELDHOLDER instance, FIELDTYPE value) {
            this.setter.accept(instance, value);
        }
    }

    public static class WorldBorderAccess {
        public static final Function<WorldBorder, List<BorderChangeListener>> listeners =
                getInstanceFieldGetter(WorldBorder.class, "f_61905_");
    }

    public static class StructureTemplateAccess {
        public static final Function<StructureTemplate, List<StructureTemplate.Palette>> palettes =
                getInstanceFieldGetter(StructureTemplate.class, "f_74482_");
    }
}
