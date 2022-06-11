package com.unrealdinnerbone.jamd;

import com.unrealdinnerbone.jamd.data.DataEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Optional;

@Mod(JAMD.MOD_ID)
public class JAMD
{
    public static final String MOD_ID = "jamd";

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<String> MAIN_WORLD = builder.comment("Main world id").define("main_world", "minecraft:overworld");

    public static final ResourceLocation DIM_ID = new ResourceLocation(MOD_ID, "mining");

    public JAMD() {
        JAMDRegistry.REGISTRIES.forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataEvent::onData);
    }

    public static String getMainWorld() {
        return MAIN_WORLD.get();
    }

    public static Optional<Level> getMainWorld(MinecraftServer server) {
        return Optional.ofNullable(server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MAIN_WORLD.get()))));
    }

    public static Optional<Level> getMining(MinecraftServer server) {
        return Optional.ofNullable(server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, DIM_ID)));
    }
}
