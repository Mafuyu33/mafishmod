package net.mafuyu33.mafishmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.mafuyu33.mafishmod.TutorialMod;
import net.mafuyu33.mafishmod.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup RUBY_GROUP= Registry.register(Registries.ITEM_GROUP,
            new Identifier(TutorialMod.MOD_ID,"ruby"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.ruby"))
                    .icon(()->new ItemStack(ModItems.RUBY)).entries((displayContext,entries)->{
                        //在这里添加新的东西
                        entries.add(ModItems.RUBY);
                        entries.add(ModItems.RAW_RUBY);
                        entries.add(ModItems.METAL_DETECTOR);
                        entries.add(ModItems.TOMATO);
                        entries.add(ModItems.COAL_BRIQUEITE);

                        entries.add(ModItems.RUBY_STAFF);

//                        entries.add(ModItems.RUBY_PICKAXE);

//                        entries.add(ModItems.RUBY_HELMET);
//                        entries.add(ModItems.RUBY_CHESTPLATE);
//                        entries.add(ModItems.RUBY_LEGGINGS);
//                        entries.add(ModItems.RUBY_BOOTS);

                        entries.add(ModBlocks.RUBY_BLOCK);
                        entries.add(ModBlocks.RAW_RUBY_BLOCK);
                        entries.add(ModBlocks.WHATE_CAT_BLOCK);
                        entries.add(ModBlocks.GOLD_MELON);


                        entries.add(ModItems.BREAD_SWORD);
                        entries.add(ModItems.BREAD_SWORD_HOT);
                        entries.add(ModItems.BREAD_SWORD_VERY_HOT);

                        entries.add(ModItems.TNT_BALL);
                        entries.add(ModItems.STONE_BALL);

                        entries.add(ModItems.FU);
                        entries.add(ModItems.IRON_FAKE);
                        entries.add(ModItems.ZHUGE);

                        entries.add(ModItems.POISON_SWORD);
                        entries.add(ModItems.FIREWORK_ARROW);

                        entries.add(ModBlocks.POTATO_TNT_PREPARE);
                        entries.add(ModBlocks.GEM_POLISHING_STATION);
                        entries.add(ModItems.STARGAZY_PIE);
                        entries.add(ModItems.TIME_STOP);
                        entries.add(ModItems.MATH_SWORD);
                        entries.add(ModItems.COLLIABLE);
                        entries.add(ModItems.LIGHTNING_BALL);
                        entries.add(ModItems.MILK_FLESH);

                        entries.add(ModItems.VR_PEN);
                        entries.add(ModItems.VR_RUBBER);
                        entries.add(ModItems.VR_RULER);
                        entries.add(ModItems.VR_COMPASSES);
                        entries.add(ModItems.VR_MAGIC);
                        entries.add(ModItems.VR_GETTING_OVER_IT);

                        entries.add(ModItems.CHEESE_BERGER);
                        entries.add(ModItems.RTX4090);
                    }).build());


    public static void registerItemGroups(){
        TutorialMod.LOGGER.info("注册一个自定义物品TAB"+TutorialMod.MOD_ID);
    }
}
