package net.mafuyu33.mafishmod.util;

import net.mafuyu33.mafishmod.TutorialMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> METAL_DETECTOR_DETECTABLE_BLOCKS =
                createTag("metal_detector_detectable_blocks");

        private static TagKey<Block> createTag(String name){
            return TagKey.of(RegistryKeys.BLOCK,new Identifier(TutorialMod.MOD_ID,name));
        }
    }

    public static class Items {
        public static final TagKey<Item> MOD_ARROW_ITEMS =
                createTag("mod_arrow_items");
        private static TagKey<Item> createTag(String name){
            return TagKey.of(RegistryKeys.ITEM,new Identifier(TutorialMod.MOD_ID,name));
        }
    }

}
