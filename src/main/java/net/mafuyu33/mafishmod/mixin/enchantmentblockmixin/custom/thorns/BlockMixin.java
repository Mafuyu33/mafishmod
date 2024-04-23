package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.thorns;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.mafuyu33.mafishmod.mixinhelper.InjectHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
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

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock implements ItemConvertible, FabricBlock {
    public BlockMixin(Settings settings) {
        super(settings);
    }
    @Inject(at = @At("HEAD"), method = "onSteppedOn")
    private void init3(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        int k = BlockEnchantmentStorage.getLevel(Enchantments.THORNS,pos);
        if (!world.isClient() && k > 0) {//如果有荆棘附魔
            entity.damage(entity.getDamageSources().cactus(),(float) k);
        }
    }
}
