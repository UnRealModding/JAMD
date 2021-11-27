package com.unrealdinnerbone.jamd.util;

import com.unrealdinnerbone.jamd.JAMD;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class SimpleTeleporter implements ITeleporter
{
    private final double x;
    private final double y;
    private final double z;

    public SimpleTeleporter(double x, double y, double z) {
        this.x = x + 0.5;
        this.y = y + 0.5;
        this.z = z + 0.5;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity repositionedEntity = repositionEntity.apply(false);
        repositionedEntity.teleportTo(x, y, z);
        return repositionedEntity;
    }


    @Override
    public boolean playTeleportSound(ServerPlayerEntity player, ServerWorld sourceWorld, ServerWorld destWorld) {
        return JAMD.playTeleportSound();
    }
}
