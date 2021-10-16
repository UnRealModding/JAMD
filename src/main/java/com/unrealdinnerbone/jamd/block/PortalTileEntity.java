package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAMDRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PortalTileEntity extends BlockEntity {

    private ResourceLocation worldId;

    public PortalTileEntity(BlockPos blockPos, BlockState blockState) {
        super(JAMDRegistry.PORTAL.get(), blockPos, blockState);
    }

    public void setWorldId(ResourceLocation worldId) {
        this.worldId = worldId;
    }

    public ResourceLocation getWorldId() {
        return worldId == null ? new ResourceLocation("minecraft", "empty") : worldId;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("world_id", getTheWorldId());
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if(tag.contains("world_id")) {
            worldId = ResourceLocation.tryParse(tag.getString("world_id"));
        }
        super.load(tag);
    }



    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putString("world_id", getTheWorldId());
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, compoundNBT);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag nbt = pkt.getTag();
        if(nbt.contains("world_id")) {
            worldId = ResourceLocation.tryParse(nbt.getString("world_id"));
        }
    }



    public String getTheWorldId() {
        return worldId == null ? "" : worldId.toString();
    }



    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putString("world_id", getTheWorldId());
        return compoundNBT;
    }

}
