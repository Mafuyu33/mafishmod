package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.mixinhelper.BlockEnchantmentHelper;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static net.minecraft.block.entity.AbstractFurnaceBlockEntity.createFuelTimeMap;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {
	protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Inject(at = @At("HEAD"), method = "getFuelTime",cancellable = true)
	private void init(ItemStack fuel, CallbackInfoReturnable<Integer> cir) {
		int k = BlockEnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT,pos);
		System.out.println(k);
		if(k>0){
			BlockPos firePos =pos;
			firePos = firePos.add(0,1,0);
			Item item = fuel.getItem();
			if(world!=null && !world.isClient) {
				world.setBlockState(firePos, Blocks.FIRE.getDefaultState(), 3);
			}
			cir.setReturnValue((Integer)createFuelTimeMap().getOrDefault(item, 0)+800*k);
		}
	}
}