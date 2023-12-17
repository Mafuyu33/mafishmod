package net.jiang.tutorialmod.Item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jiang.tutorialmod.Item.custom.MetalDetectoritem;
import net.jiang.tutorialmod.TutorialMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item RUBY = registerItem("ruby",new Item(new FabricItemSettings()));
    public static final Item RAW_RUBY=registerItem("raw_ruby",new Item(new FabricItemSettings()));
    public static final Item COOL_GLASS=registerItem("cool_glass",new Item(new FabricItemSettings()));
    public static final Item METAL_DETECTOR=registerItem("metal_detector",
            new MetalDetectoritem(new FabricItemSettings().maxDamage(64)));
    public static final Item TOMATO = registerItem("tomato",new Item(new FabricItemSettings().food(ModFoodComponents.TOMATO)));
    public static final Item COAL_BRIQUEITE = registerItem("coal_briquette",
            new Item(new FabricItemSettings().food(ModFoodComponents.TOMATO)));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries){
        //在这里注册
        entries.add(RUBY);
        entries.add(RAW_RUBY);
        entries.add(COOL_GLASS);
    }

    public static Item registerItem(String name,Item item){
        return Registry.register(Registries.ITEM,new Identifier(TutorialMod.MOD_ID,name),item);
    }

    public static void registerModItems(){
        TutorialMod.LOGGER.info("注册MOD物品"+TutorialMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
