package com.unrealdinnerbone.jamd.data;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public record CodecTypedGenerator<T>(DataGenerator generator, String modId, ResourceKey<Registry<T>> key, Codec<T> codec) implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final RegistryAccess ACCESS = RegistryAccess.BUILTIN.get();
    private static final RegistryOps<JsonElement> JSON = RegistryOps.create(JsonOps.INSTANCE, ACCESS);


    @Override
    public void run(CachedOutput cachedOutput) throws IOException {
        Registry<T> registry = ACCESS.registryOrThrow(key);

        DataGenerator.PathProvider pathProvider = this.generator.createPathProvider(DataGenerator.Target.DATA_PACK, key.location().getPath());

        registry.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(modId))
                .forEach(entry -> dumpValue(pathProvider.json(entry.getKey().location()), cachedOutput, entry.getValue()));
    }

    @Override
    public String getName() {
        return key.registry().toString();
    }

    private void dumpValue(Path path, CachedOutput cachedOutput,  T object) {
        codec().encodeStart(JSON, object)
                .resultOrPartial((p_206405_) -> LOGGER.error("Couldn't serialize element {}: {}", path, p_206405_))
                .ifPresent(jsonElement -> {
                    try {
                        DataProvider.saveStable(cachedOutput, jsonElement, path);
                    }catch(IOException ioexception) {
                        LOGGER.error("Couldn't save element {}", path, ioexception);
                    }
                });
    }
}
