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
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
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
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if(ConfigHelper.isQinNa()) {
            if (entity instanceof LlamaEntity && !world.isClient()) {//羊驼
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_LLAMA_HURT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                entity.dropItem(ModItems.LLAMA_ITEM);
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
            if (entity instanceof VillagerEntity && !world.isClient()) {//村民
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_VILLAGER_HURT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                entity.dropItem(ModItems.VILLAGER_ITEM);
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
            if (entity instanceof IronGolemEntity && !world.isClient()) {//铁傀儡
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                entity.dropItem(ModItems.IRON_GOLEM_ITEM);
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        return ActionResult.PASS;
    }
}
