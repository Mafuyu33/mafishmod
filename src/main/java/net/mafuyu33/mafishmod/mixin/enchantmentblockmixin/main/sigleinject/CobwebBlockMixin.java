package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.main.sigleinject;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CobwebBlock.class)
public abstract class CobwebBlockMixin extends Block {
    protected CobwebBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "onEntityCollision")//存储方块的附魔
    private void init1(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        int k = BlockEnchantmentStorage.getLevel(Enchantments.THORNS,pos);
        if (!world.isClient() && k > 0) {//如果有荆棘附魔
            entity.damage(entity.getDamageSources().cactus(),(float) k);
        }
    }
}
