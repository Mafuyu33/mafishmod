package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberMixin extends ProjectileEntity {
	@Shadow @Nullable public abstract PlayerEntity getPlayerOwner();

	public FishingBobberMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	@Unique
	private int delayTimer = 0;
	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		PlayerEntity playerEntity = getPlayerOwner();
		if (playerEntity != null) {
			ItemStack itemStack = playerEntity.getMainHandStack();
			if(itemStack.getItem() == Items.FISHING_ROD) {
				int k = EnchantmentHelper.getLevel(Enchantments.FROST_WALKER, itemStack);
				if (k > 0) {
					World world = this.getWorld();
					BlockPos blockPos = this.getBlockPos();
					FluidState fluidState = world.getFluidState(blockPos);
					if (fluidState.isIn(FluidTags.WATER)) {
						this.addVelocity(0,0.3,0);
						if (delayTimer < 1) {
							delayTimer++;
						} else {
							world.setBlockState(blockPos, Blocks.FROSTED_ICE.getDefaultState(), 3);
							delayTimer = 0;
						}
					}
				}
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "tick")
	private void init1(CallbackInfo info) {
		PlayerEntity playerEntity = getPlayerOwner();
		if (playerEntity != null) {
			ItemStack itemStack = playerEntity.getMainHandStack();
			if(itemStack.getItem() == Items.FISHING_ROD) {
				int k = EnchantmentHelper.getLevel(ModEnchantments.BAD_LUCK_OF_SEA, itemStack);
				if (k > 0) {
					World world = this.getWorld();
					BlockPos blockPos = this.getBlockPos();
					FluidState fluidState = world.getFluidState(blockPos);
					if (fluidState.isIn(FluidTags.WATER)) {
						this.addVelocity(0,1,0);
					}
				}
			}
		}
	}
}