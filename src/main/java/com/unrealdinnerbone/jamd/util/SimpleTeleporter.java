package com.unrealdinnerbone.jamd.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;


public record SimpleTeleporter(double x, double y, double z) implements ITeleporter {
    public static SimpleTeleporter createBasic(double x, double y, double z) {
        return new SimpleTeleporter(x + 0.5, y + 0.5, z + 0.5);
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity repositionedEntity = repositionEntity.apply(false);
        repositionedEntity.teleportTo(x, y, z);
        return repositionedEntity;
    }
}
