package net.mafuyu33.mafishmod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mafuyu33.mafishmod.item.custom.*;
import net.mafuyu33.mafishmod.TutorialMod;
import net.mafuyu33.mafishmod.item.vrcustom.*;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item RUBY = registerItem("ruby",new Item(new FabricItemSettings().fireproof()));
    public static final Item RAW_RUBY=registerItem("raw_ruby",new Item(new FabricItemSettings().fireproof()));
//    public static final Item COOL_GLASS=registerItem("cool_glass",
//            new AppleVisionProItem(ModArmorMaterials.RUBY, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
//    public static final Item COOL_GLASS2=registerItem("cool_glass2",
//            new AppleVisionProItem(ModArmorMaterials.RUBY, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item METAL_DETECTOR=registerItem("metal_detector",
            new MetalDetectorItem(new FabricItemSettings().maxDamage(64)));

    public static final Item RUBY_STAFF=registerItem("ruby_staff",
            new RubyStuffItem(new FabricItemSettings().maxCount(1)));
    public static final Item RUBY_PICKAXE =registerItem("ruby_pickaxe",
            new PickaxeItem(ModToolMaterial.RUBY,2,2f, new FabricItemSettings()));

    public static final Item RUBY_HELMET =registerItem("ruby_helmet",
            new ModArmorItem(ModArmorMaterials.RUBY,ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item RUBY_CHESTPLATE = registerItem("ruby_chestplate",
            new ArmorItem(ModArmorMaterials.RUBY, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item RUBY_LEGGINGS = registerItem("ruby_leggings",
            new ArmorItem(ModArmorMaterials.RUBY, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item RUBY_BOOTS = registerItem("ruby_boots",
            new ArmorItem(ModArmorMaterials.RUBY, ArmorItem.Type.BOOTS, new FabricItemSettings()));

    public static final Item TOMATO = registerItem("tomato",new Item(new FabricItemSettings().food(ModFoodComponents.TOMATO)));
    public static final Item COAL_BRIQUEITE = registerItem("coal_briquette",
            new Item(new FabricItemSettings().food(ModFoodComponents.TOMATO)));

    public static final Item TNT_BALL = registerItem("tnt_ball", new TNTBallItem(new FabricItemSettings()));
    public static final Item STONE_BALL = registerItem("stone_ball", new StoneBallItem(new FabricItemSettings()));

//    public static final Item APPLE_VISION_PRO = registerItem("apple_vision_pro",
//            new AppleVisionProItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET,new FabricItemSettings()));

    public static final Item BREAD_SWORD = registerItem("bread_sword",
            new BreadSwordItem(ToolMaterials.STONE, 3, -2.4f,
                    new FabricItemSettings().food(ModFoodComponents.BREAD_SWORD)));

    public static final Item BREAD_SWORD_HOT = registerItem("bread_sword_hot",
            new BreadSwordHotItem(ToolMaterials.IRON, 3, -2.4f,
                    new FabricItemSettings().food(ModFoodComponents.BREAD_SWORD_HOT)));

    public static final Item BREAD_SWORD_VERY_HOT = registerItem("bread_sword_very_hot",
            new BreadSwordVeryHotItem(ToolMaterials.DIAMOND, 3, -2.4f,
                    new FabricItemSettings().food(ModFoodComponents.BREAD_SWORD_VERY_HOT)));

    public static final Item FU = registerItem("fu",
            new FuItem(ToolMaterials.IRON, 6.0F, -3.1F, new FabricItemSettings()));
    public static final Item IRON_FAKE = registerItem("iron_fake",new Item(new FabricItemSettings()));

    public static final Item ZHUGE = registerItem("zhuge",new ZhuGeItem(new FabricItemSettings().maxCount(1).maxDamage(465)));

    public static final Item POISON_SWORD = registerItem("poison_sword",
            new PoisonSwordItem(ToolMaterials.DIAMOND, 3, -2.4f,
                    new FabricItemSettings().food(ModFoodComponents.POISON_SWORD)));

    public static final Item FIREWORK_ARROW = registerItem("firework_arrow",new Item(new FabricItemSettings()));
    public static final Item STARGAZY_PIE = registerItem("stargazy_pie",
            new StargazyPieItem(new FabricItemSettings().food(ModFoodComponents.STARGAZY_PIE)));

    public static final Item TIME_STOP = registerItem("time_stop",
            new TimeStopItem(new FabricItemSettings().maxCount(1)));
    public static final Item MATH_SWORD = registerItem("math_sword",
            new MathSwordItem(ToolMaterials.NETHERITE, 7, -2.4f, new FabricItemSettings()));
    public static final Item COLLIABLE = registerItem("colliable",
            new ColliableItem(new FabricItemSettings().maxCount(1)));
    public static final Item LIGHTNING_BALL = registerItem("lightning_ball", new LightningBallItem(new FabricItemSettings()));
    public static final Item MILK_FLESH = registerItem("milk_flesh",new MilkFleshItem(new FabricItemSettings().food(ModFoodComponents.MILK_FLESH)));

    public static final Item CHEESE_BERGER = registerItem("cheese_berger",
            new CheeseBergerItem(new FabricItemSettings().food(ModFoodComponents.CHEESE_BERGER)));
    public static final Item VR_PEN = registerItem("vr_pen", new VrPenItem(new FabricItemSettings().maxCount(1)));
    public static final Item VR_RUBBER = registerItem("vr_rubber", new VrRubberItem(new FabricItemSettings().maxCount(1)));
    public static final Item VR_RULER = registerItem("vr_ruler", new VrRulerItem(new FabricItemSettings().maxCount(1)));
    public static final Item VR_COMPASSES = registerItem("vr_compasses", new VrCompassesItem(new FabricItemSettings().maxCount(1)));
    public static final Item VR_MAGIC = registerItem("vr_magic", new VrMagicItem(new FabricItemSettings().maxCount(1)));
    public static final Item RTX4090 = registerItem("rtx4090",new RTX4090Item(ToolMaterials.NETHERITE,2,2f, new FabricItemSettings()));
    public static final Item VR_GETTING_OVER_IT = registerItem("vr_getting_over_it", new VrGettingOverItItem(new FabricItemSettings().maxCount(1)));


    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries){
        //放到材料表里
        entries.add(RUBY);
        entries.add(RAW_RUBY);
    }

    public static Item registerItem(String name,Item item){
        return Registry.register(Registries.ITEM,new Identifier(TutorialMod.MOD_ID,name),item);
    }

    public static void registerModItems(){
        TutorialMod.LOGGER.info("注册MOD物品"+TutorialMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
