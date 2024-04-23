package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.main;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(World.class)
public abstract class WorldMixin {
	@Shadow public abstract boolean isClient();

	@Inject(at = @At("HEAD"), method = "breakBlock")
	private void init(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
		if (!this.isClient()) {
			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(pos), new NbtList())) {
				BlockEnchantmentStorage.removeBlockEnchantment(pos.toImmutable());//删除信息
			}
		}
	}
}