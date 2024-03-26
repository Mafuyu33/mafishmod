package net.mafuyu33.mafishmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RUBY_BLOCK);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_RUBY_BLOCK);
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.WHATE_CAT_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
//        itemModelGenerator.register(ModItems.RUBY, Models.GENERATED);
//        itemModelGenerator.register(ModItems.RAW_RUBY, Models.GENERATED);
//
//        itemModelGenerator.register(ModItems.COAL_BRIQUEITE, Models.GENERATED);
//        itemModelGenerator.register(ModItems.TOMATO, Models.GENERATED);
//        itemModelGenerator.register(ModItems.METAL_DETECTOR, Models.GENERATED);
        itemModelGenerator.register(ModItems.RUBY_PICKAXE, Models.HANDHELD);

        itemModelGenerator.registerArmor(((ArmorItem)ModItems.RUBY_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem)ModItems.RUBY_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem)ModItems.RUBY_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem)ModItems.RUBY_BOOTS));
    }
}
