package net.mafuyu33.mafishmod.mixin;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.mafuyu33.mafishmod.mixinhelper.BlockEnchantmentHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
		if (!world.isClient) {
			System.out.println(itemStack.getEnchantments());
			if (!Objects.equals(itemStack.getEnchantments(), new NbtList())) {
//				System.out.println("添加");
				NbtList enchantments = itemStack.getEnchantments(); // 获取物品栈上的附魔信息列表
				BlockEnchantmentHelper.storeEnchantment(pos,enchantments);// 将附魔信息列表存储
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "onBroken")//删除方块的附魔
	private void init2(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci){
		if (!world.isClient()) {
			if (!Objects.equals(BlockEnchantmentHelper.getEnchantment(pos), new NbtList())) {
//				System.out.println("删除！");
				BlockEnchantmentHelper.storeEnchantment(pos,new NbtList());// 将附魔信息列表存储
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "onDestroyedByExplosion")//删除方块的附魔
	private void init3(World world, BlockPos pos, Explosion explosion, CallbackInfo ci){
		if (!world.isClient()) {
			if (!Objects.equals(BlockEnchantmentHelper.getEnchantment(pos), new NbtList())) {
//				System.out.println("删除！");
				BlockEnchantmentHelper.storeEnchantment(pos,new NbtList());// 将附魔信息列表存储
			}
		}
	}
}