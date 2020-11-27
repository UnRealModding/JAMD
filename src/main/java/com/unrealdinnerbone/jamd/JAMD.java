package com.unrealdinnerbone.jamd;

import com.unrealdinnerbone.jamd.data.DataEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

@Mod(JAMD.MOD_ID)
public class JAMD
{
    public static final String MOD_ID = "jamd";

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ResourceLocation DIM_ID = new ResourceLocation(MOD_ID, "mining");

    public JAMD() {
        JAMDRegistry.REGISTRIES.forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onBiomesLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataEvent::onData);
    }

    public void onBiomesLoad(BiomeLoadingEvent biomeLoadingEvent) {
        if (biomeLoadingEvent.getName() != null && biomeLoadingEvent.getName().toString().equals(DIM_ID.toString())) {
            biomeLoadingEvent.getGeneration().getStructures().clear();
            biomeLoadingEvent.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).clear();
            biomeLoadingEvent.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).clear();
        }
    }
}
