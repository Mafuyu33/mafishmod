package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.knockback;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock implements ItemConvertible, FabricBlock {
	public BlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(at = @At("HEAD"), method = "onSteppedOn")
	private void init(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK,pos);
		if (!world.isClient() && k > 0) {//如果有击退附魔
			entity.addVelocity(0,k*0.5,0);
		}
		if (world.isClient() && k > 0 && entity instanceof PlayerEntity player) {//如果有击退附魔
			player.addVelocity(0,k*0.5,0);
		}
	}
}