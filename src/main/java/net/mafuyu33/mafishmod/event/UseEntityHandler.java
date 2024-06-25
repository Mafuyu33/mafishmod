package net.mafuyu33.mafishmod.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.mafuyu33.mafishmod.config.ConfigHelper;
import net.mafuyu33.mafishmod.item.ModItems;
import net.mafuyu33.mafishmod.item.vrcustom.VrMagicItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.LlamaEntity;
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

public class UseEntityHandler implements UseEntityCallback {
    private static boolean hasUsed = false; // 添加一个变量来标记是否已经攻击过鸡
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if(ConfigHelper.isQinNa()) {
            if (entity instanceof LlamaEntity && !world.isClient()) {
                entity.dropItem(ModItems.LLAMA_ITEM);
                entity.remove(Entity.RemovalReason.DISCARDED);
                hasUsed = true;
            }
        }
        return ActionResult.PASS;
    }
    public static boolean hasUsed() {
        return hasUsed;
    }
    public static void changeAttacked() {
        hasUsed =false;
    }
}
