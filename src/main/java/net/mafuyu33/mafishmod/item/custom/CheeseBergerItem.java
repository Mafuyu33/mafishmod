package net.mafuyu33.mafishmod.item.custom;

import net.mafuyu33.mafishmod.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CheeseBergerItem extends Item {


    public CheeseBergerItem(Settings settings) {
        super(settings);
    }
    private static final int COOLDOWN_TICKS = 200; // 10秒内只播放一次

    private int cooldown = 0;


    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(entity instanceof CatEntity) {
            if(user.getWorld().isClient) {
                user.getWorld().playSound(user, entity.getBlockPos(), ModSounds.CHEESE_BERGER_CAT, SoundCategory.MASTER);
            }
            entity.addVelocity(0,0.3,0);
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient && entity instanceof PlayerEntity) {
            if (cooldown > 0) {
                cooldown--;
            } else {
                if (hasNearbyCat(world, entity)) {
                    playCatSound(world, entity);
                    cooldown = COOLDOWN_TICKS;
                }
            }
        }
    }

    private boolean hasNearbyCat(World world, Entity entity) {
        return !world.getEntitiesByClass(CatEntity.class, entity.getBoundingBox().expand(8.0), cat -> true).isEmpty();
    }

    private void playCatSound(World world, Entity entity) {
        // 播放声音
        if(entity instanceof PlayerEntity) {
            world.playSound((PlayerEntity) entity, entity.getX(), entity.getY(), entity.getZ(), ModSounds.CHEESE_BERGER_MAN, SoundCategory.MASTER, 1.0f, 1.0f);
        }
    }
}


