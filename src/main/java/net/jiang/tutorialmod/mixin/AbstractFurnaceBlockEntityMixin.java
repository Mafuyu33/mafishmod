package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.mixinhelper.BlockEnchantmentHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {
	protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, Boolean isFortune) {
		super(blockEntityType, blockPos, blockState);
	}
	@Inject(at = @At("HEAD"), method = "getFuelTime",cancellable = true)
	private void init1(ItemStack fuel, CallbackInfoReturnable<Integer> cir) {
		int k = BlockEnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT,pos);
		if(k>0){//火焰附加
			BlockPos firePos =pos;
			firePos = firePos.add(0,1,0);
			if(world!=null && !world.isClient) {
				world.setBlockState(firePos, Blocks.FIRE.getDefaultState(), 3);
			}
			cir.setReturnValue(-1);
		}
	}
}