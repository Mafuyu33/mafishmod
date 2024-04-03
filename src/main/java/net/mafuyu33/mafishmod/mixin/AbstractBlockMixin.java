package net.mafuyu33.mafishmod.mixin;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.mafuyu33.mafishmod.mixinhelper.BlockEnchantmentInjectHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin implements ToggleableFeature {
    @Inject(at = @At("HEAD"), method = "onEntityCollision")//荆棘附魔，踩上去受伤
    private void init3(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        int k = BlockEnchantmentStorage.getLevel(Enchantments.THORNS,pos);
        if (!world.isClient() && k > 0) {//如果有荆棘附魔
            entity.damage(entity.getDamageSources().cactus(),(float) k);
        }
    }

}
