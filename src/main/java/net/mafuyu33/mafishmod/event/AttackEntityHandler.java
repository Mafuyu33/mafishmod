package net.mafuyu33.mafishmod.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.mafuyu33.mafishmod.item.vrcustom.VrMagicItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AttackEntityHandler implements AttackEntityCallback {
    private static boolean hasAttacked = false; // 添加一个变量来标记是否已经攻击过鸡
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if(entity instanceof ChickenEntity && !world.isClient()){
            player.sendMessage(Text.literal("哎呦你干嘛"));
            hasAttacked = true;
        }
        if(!world.isClient && VrMagicItem.isUsingMagic){
            BlockPos blockPos = entity.getBlockPos();
            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(entity.getWorld());
            if (lightningEntity != null) {
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                lightningEntity.setChanneler(player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player : null);
                entity.getWorld().spawnEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                entity.playSound(soundEvent, 5, 1.0F);
            }
            entity.kill();
        }

        return ActionResult.PASS;
    }
    public static boolean hasAttacked() {
        return hasAttacked;
    }
    public static void changeAttacked() {
        hasAttacked =false;
    }
}
