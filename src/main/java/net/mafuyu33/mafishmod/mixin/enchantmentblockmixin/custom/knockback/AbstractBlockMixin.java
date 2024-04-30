package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.knockback;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin implements ToggleableFeature {
    @Inject(at = @At("HEAD"), method = "onEntityCollision")
    private void init3(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        int k = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK,pos);
        if (!world.isClient() && k > 0) {
            entity.addVelocity(0,k*0.5,0);
        }
        if (world.isClient() && k > 0 && entity instanceof PlayerEntity player) {//如果有击退附魔
            player.addVelocity(0,k*0.5,0);
        }
    }
}
