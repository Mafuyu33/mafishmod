package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.main;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.mafuyu33.mafishmod.mixinhelper.InjectHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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


    @Inject(at = @At("HEAD"), method = "onPlaced")//存储方块的附魔
    private void init1(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack, CallbackInfo info) {
        InjectHelper.onPlacedInject(world,itemStack,pos);
    }

    @Inject(at = @At("HEAD"), method = "onBroken")//删除方块的附魔
    private void init2(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci){
        if (!world.isClient()) {
            if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(pos), new NbtList())) {
                BlockEnchantmentStorage.removeBlockEnchantment(pos.toImmutable());//删除信息

                //给掉落的方块附魔
//                BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
//                Block.dropStacks(state, (World) world, pos, blockEntity, null, ItemStack.EMPTY);
            }
        }
    }
    @Inject(at = @At("TAIL"), method = "onDestroyedByExplosion")//删除方块的附魔
    private void init4(World world, BlockPos pos, Explosion explosion, CallbackInfo ci){
        if (!world.isClient()) {
            if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(pos), new NbtList())) {
                BlockEnchantmentStorage.removeBlockEnchantment(pos.toImmutable());//删除信息
            }
        }
    }
}
