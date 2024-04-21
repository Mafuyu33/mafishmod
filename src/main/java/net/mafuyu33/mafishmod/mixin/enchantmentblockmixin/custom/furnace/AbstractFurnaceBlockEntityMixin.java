package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.furnace;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
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
		int k = BlockEnchantmentStorage.getLevel(Enchantments.FIRE_ASPECT,pos);
		if(k>0){//火焰附加
			BlockPos firePos =pos;
			firePos = firePos.add(0,1,0);
			if(world!=null && !world.isClient) {
				world.setBlockState(firePos, Blocks.FIRE.getDefaultState(), 3);
			}
			cir.setReturnValue(-1);
		}
	}



	@Inject(method = "craftRecipe",at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V"))//时运烧矿
	private static void init2(DynamicRegistryManager registryManager, RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemStack = (ItemStack)slots.get(0);
		int k = EnchantmentHelper.getLevel(Enchantments.FORTUNE, itemStack);
		if (k > 0) {
			ItemStack itemStack3 = (ItemStack)slots.get(2);
			itemStack3.increment(k);
		}
	}
	@Inject(method = "craftRecipe",at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;",ordinal = 0))
	private static void init3(DynamicRegistryManager registryManager, RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemStack = (ItemStack)slots.get(0);
		int k = EnchantmentHelper.getLevel(Enchantments.FORTUNE, itemStack);
		if (k > 0) {
			ItemStack itemStack3 = (ItemStack)slots.get(2);
			itemStack3.increment(k);
		}
	}

}