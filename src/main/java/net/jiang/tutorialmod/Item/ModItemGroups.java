package net.jiang.tutorialmod.Item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.jiang.tutorialmod.TutorialMod;
import net.jiang.tutorialmod.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
                        entries.add(ModItems.COOL_GLASS);

                        entries.add(Items.DIAMOND);

                        entries.add(ModBlocks.RUBY_BLOCK);
                        entries.add(ModBlocks.RAW_RUBY_BLOCK);
                        entries.add(ModBlocks.WHATE_CAT_BLOCK);

                    }).build());


    public static void registerItemGroups(){
        TutorialMod.LOGGER.info("注册一个自定义物品TAB"+TutorialMod.MOD_ID);
    }
}
