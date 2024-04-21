package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.fasthopper;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	@Inject(at = @At("RETURN"), method = "insertAndExtract")
	private static void init1(World world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.QUICK_CHARGE,pos);//漏斗的快速装填
//		System.out.println("传递一次");
		if(k>0){
//			System.out.println("设置冷却");
			blockEntity.setTransferCooldown(0);
		}
	}

	@Inject(at = @At("HEAD"), method = "insert",cancellable = true)
	private static void init2(World world, BlockPos pos, BlockState state, Inventory inventory, CallbackInfoReturnable<Boolean> cir){
		int k = BlockEnchantmentStorage.getLevel(Enchantments.BINDING_CURSE,pos);//漏斗的绑定诅咒
		if(k>0){
			System.out.println("取消传递！");
			cir.cancel();
		}
	}
}