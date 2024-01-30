package net.jiang.tutorialmod.item.custom;

import net.jiang.tutorialmod.entity.FireworkArrowEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class FireworkArrowItem extends TippedArrowItem {

    public FireworkArrowItem(Settings settings) {
        super(settings);
    }
}
