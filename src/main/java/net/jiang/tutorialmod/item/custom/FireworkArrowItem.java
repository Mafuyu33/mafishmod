package net.jiang.tutorialmod.item.custom;

import net.jiang.tutorialmod.entity.FireworkArrowEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class FireworkArrowItem extends ArrowItem {
    public FireworkArrowItem(Item.Settings settings) {
        super(settings);
    }
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new FireworkArrowEntity(world, shooter);
    }
}
