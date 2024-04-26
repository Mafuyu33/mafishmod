package net.mafuyu33.mafishmod.enchantment;

import net.mafuyu33.mafishmod.TutorialMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {

    public static Enchantment BAD_LUCK_OF_SEA = register("bad_luck_of_sea",
            new CursedLv3(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET}));

    public static Enchantment EIGHT_GODS_PASS_SEA = register("eight_gods_pass_sea",
            new NormalLv1(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET}));

    public static Enchantment KILL_CHICKEN_GET_EGG = register("kill_chicken_get_egg",
            new NormalLv1(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment GO_TO_SKY = register("go_to_sky",
            new TreasureLv1(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));

    public static Enchantment GONG_XI_FA_CAI = register("gong_xi_fa_cai",
            new NormalLv5(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));

    public static Enchantment MERCY = register("mercy",
            new NormalLv1(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));

    public static Enchantment KILL_MY_HORSE = register("kill_my_horse",
            new TreasureLv1(Enchantment.Rarity.RARE , EnchantmentTarget.ARMOR,new EquipmentSlot[]{}));
    public static Enchantment KILL_MY_HORSE_PLUS = register("kill_my_horse_plus",
            new TreasureLv1(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.ARMOR, new EquipmentSlot[]{}));
    public static Enchantment HOT_POTATO = register("hot_potato",
            new CursedLv1(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment VERY_EASY = register("very_easy",
            new CursedLv5(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment REVERSE = register("reverse",
            new CursedLv1(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment PAY_TO_PLAY = register("pay_to_play",
            new NormalLv5(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment FEAR = register("fear",
            new NormalLv5(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment MUTE = register("mute",
            new CursedLv1(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD}));
    public static Enchantment SLIPPERY = register("slippery",
            new CursedTreasureLv1(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.BREAKABLE,new EquipmentSlot[]{}));
    public static Enchantment NEVER_GONNA = register("never_gonna",
            new CursedLv1(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.DIGGER,new EquipmentSlot[]{}));
    public static Enchantment RESONANCE = register("resonance",
            new TreasureLv1(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.BREAKABLE,new EquipmentSlot[]{}));
    public static Enchantment NO_BLAST_PROTECTION = register("no_blast_protection",
            new CursedLv1(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.ARMOR,new EquipmentSlot[]{}));
    public static Enchantment A_LEAF = register("a_leaf",
            new CursedLv1(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR_HEAD,new EquipmentSlot[]{}));



    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, new Identifier(TutorialMod.MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments(){
        System.out.println("注册附魔");
    }
}
