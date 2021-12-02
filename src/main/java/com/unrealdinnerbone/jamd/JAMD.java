package com.unrealdinnerbone.jamd;

import com.unrealdinnerbone.jamd.data.DataEvent;
import com.unrealdinnerbone.jamd.world.CustomFlatLevelSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(JAMD.MOD_ID)
public class JAMD
{
    public static final String MOD_ID = "jamd";

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.BooleanValue FLOWERS = builder.comment("Stop mods from adding custom flowers").define("flowers", true);
    private static final ForgeConfigSpec.BooleanValue STRUCTURES = builder.comment("Stop mods from adding surface structures").define("surface_structures", true);
    private static final ForgeConfigSpec.BooleanValue ENTITIES = builder.comment("Stop mods from adding entities").define("entities", true);
    private static final ForgeConfigSpec.BooleanValue LAKES = builder.comment("Stop mods from adding lakes").define("lakes", true);

    public static final ResourceLocation DIM_ID = new ResourceLocation(MOD_ID, "mining");

    public JAMD() {
        Registry.register(Registry.CHUNK_GENERATOR, DIM_ID, CustomFlatLevelSource.CODEC);

        JAMDRegistry.REGISTRIES.forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onBiomesLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataEvent::onData);
    }

    public void onBiomesLoad(BiomeLoadingEvent biomeLoadingEvent) {
        if (biomeLoadingEvent.getName() != null && biomeLoadingEvent.getName().toString().equals(DIM_ID.toString())) {
            if(FLOWERS.get()) {
                biomeLoadingEvent.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION).clear();
            }
            if(STRUCTURES.get()) {
                biomeLoadingEvent.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).clear();
                biomeLoadingEvent.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).clear();
            }
            if(ENTITIES.get()) {
                biomeLoadingEvent.getSpawns().getSpawnerTypes().forEach(spawnerType -> biomeLoadingEvent.getSpawns().getSpawner(spawnerType).clear());
            }
            if(LAKES.get()) {
                biomeLoadingEvent.getGeneration().getFeatures(GenerationStep.Decoration.LAKES).clear();
            }
        }
    }
}
