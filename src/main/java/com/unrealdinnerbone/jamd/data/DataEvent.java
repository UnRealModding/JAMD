package com.unrealdinnerbone.jamd.data;

import com.mojang.datafixers.util.Pair;
import com.unrealdinnerbone.jamd.JAMD;
import com.unrealdinnerbone.jamd.JAMDRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeLootTableProvider;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class DataEvent {

    public static void onData(GatherDataEvent event) {
        event.getGenerator().addProvider(new Recipe(event.getGenerator()));
        event.getGenerator().addProvider(new BlockState(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(new Item(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(new LootTable(event.getGenerator()));
    }

    public static class LootTable extends ForgeLootTableProvider {

        public LootTable(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, net.minecraft.loot.LootTable.Builder>>>, LootParameterSet>> getTables() {
            return Collections.singletonList(Pair.of(BlockLootTable::new, LootParameterSets.BLOCK));
        }

        public static class BlockLootTable extends BlockLootTables {

            @Override
            public void addTables() {
                registerDropSelfLootTable(JAMDRegistry.MINE_PORTAL_BLOCK.get());
            }

            protected Iterable<Block> getKnownBlocks() {
                return Collections.singleton(JAMDRegistry.MINE_PORTAL_BLOCK.get());
            }


        }
    }

    public static class Item extends net.minecraftforge.client.model.generators.ItemModelProvider {

        public Item(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, JAMD.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            cubeAll(JAMDRegistry.MINE_PORTAL_BLOCK.get().getRegistryName().getPath(),new ResourceLocation(JAMD.MOD_ID, "block/mine_portal_block"));
        }

        public void itemGenerated(net.minecraft.item.Item item, ResourceLocation texture) {
            getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", texture);
        }

    }

    public static class BlockState extends BlockStateProvider {

        public BlockState(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, JAMD.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlock(JAMDRegistry.MINE_PORTAL_BLOCK.get());
        }
    }

    public static class Recipe extends RecipeProvider {

        public Recipe(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            ShapedRecipeBuilder.shapedRecipe(JAMDRegistry.MINE_PORTAL_BLOCK_ITEM::get)
                    .patternLine("OOO")
                    .patternLine("OEO")
                    .patternLine("OOO")
                    .key('O', Tags.Items.OBSIDIAN)
                    .key('E', Items.DIAMOND_PICKAXE)
                    .addCriterion("has_pick", hasItem(Items.DIAMOND_PICKAXE))
                    .addCriterion("has_obsidian", hasItem(Items.OBSIDIAN))
                    .build(consumer);
        }
    }
}
