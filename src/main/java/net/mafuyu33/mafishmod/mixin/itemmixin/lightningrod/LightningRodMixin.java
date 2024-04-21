package net.mafuyu33.mafishmod.mixin.itemmixin.lightningrod;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public abstract class LightningRodMixin {

    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    private void init(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        PlayerEntity user = world.getClosestPlayer(pos.getX(),pos.getY(),pos.getZ(),10f,true);
        int k = BlockEnchantmentStorage.getLevel(Enchantments.CHANNELING, pos);
        if (k > 0) {
            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
            if (lightningEntity != null) {
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
                lightningEntity.setChanneler(user instanceof ServerPlayerEntity ? (ServerPlayerEntity) user : null);
                world.spawnEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                world.playSound(user,pos,soundEvent, SoundCategory.BLOCKS,5, 1.0F);
            }
        }
    }
}
