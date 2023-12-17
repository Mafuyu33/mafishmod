package net.jiang.tutorialmod.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.jiang.tutorialmod.TutorialMod;
import net.jiang.tutorialmod.block.custom.SoundBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    //这里添加新的block
    public static final Block RUBY_BLOCK=registerBlock("ruby_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block RAW_RUBY_BLOCK=registerBlock("raw_ruby_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));





    public static final Block WHATE_CAT_BLOCK=registerBlock("white_cat_block",
            new SoundBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));








    private static Block registerBlock(String name,Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK,new Identifier(TutorialMod.MOD_ID,name),block);
    }


    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM,new Identifier(TutorialMod.MOD_ID,name),
                new BlockItem(block,new FabricItemSettings()));
    }

    public static void registerModBlocks(){
        TutorialMod.LOGGER.info("注册一个方块"+ TutorialMod.MOD_ID);
    }
}
