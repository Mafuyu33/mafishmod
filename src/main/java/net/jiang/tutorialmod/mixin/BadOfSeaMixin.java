package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class BadOfSeaMixin extends Entity implements Ownable {

	@Shadow public abstract ItemStack getStack();

	public BadOfSeaMixin(EntityType<?> type, World world) {
		super(type, world);
	}

@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {//实现丢出去的钓鱼竿实体被水排斥
		ItemStack itemStack = this.getStack();
		Item item = itemStack.getItem();
		World world = this.getWorld();
		BlockPos blockPos = this.getBlockPos();
		FluidState fluidState = world.getFluidState(blockPos);
		if(fluidState.isIn(FluidTags.WATER) & item == Items.FISHING_ROD
				& EnchantmentHelper.getLevel(ModEnchantments.BAD_LUCK_OF_SEA,itemStack)>0){
			this.setVelocity(0,0.5,0);
		}
	}

}