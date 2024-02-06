package net.jiang.tutorialmod.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
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

        return ActionResult.PASS;
    }
    public static boolean hasAttacked() {
        return hasAttacked;
    }
    public static void changeAttacked() {
        hasAttacked =false;
    }
}
