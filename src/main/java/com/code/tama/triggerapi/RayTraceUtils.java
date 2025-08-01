/* (C) TAMA Studios 2025 */
package com.code.tama.triggerapi;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RayTraceUtils {
    public static EntityHitResult rayTraceEntity(Entity source, double maxDistance) {
        Vec3 start = source.getEyePosition(1.0F);
        Vec3 look = source.getViewVector(1.0F);
        Vec3 end = start.add(look.scale(maxDistance));
        return source
                .level()
                .getEntities(
                        source,
                        source.getBoundingBox()
                                .expandTowards(look.scale(maxDistance))
                                .inflate(1.0),
                        e -> true)
                .stream()
                .map(e -> new EntityHitResult(e, start.add(look.scale(start.distanceTo(e.getPosition(1.0F))))))
                .filter(hit -> hit.getEntity().isPickable())
                .min((a, b) -> (int) (start.distanceToSqr(a.getLocation()) - start.distanceToSqr(b.getLocation())))
                .orElse(null);
    }

    public static HitResult rayTraceFromEntity(Entity entity, double maxDistance, boolean hitFluids) {
        Vec3 eyePos = entity.getEyePosition(1.0F);
        Vec3 lookVec = entity.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(lookVec.x * maxDistance, lookVec.y * maxDistance, lookVec.z * maxDistance);
        return entity.level()
                .clip(new ClipContext(
                        eyePos,
                        endPos,
                        ClipContext.Block.OUTLINE,
                        hitFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE,
                        entity));
    }
}
