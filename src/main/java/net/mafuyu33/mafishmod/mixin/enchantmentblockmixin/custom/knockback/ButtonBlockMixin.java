package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.knockback;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.CactusBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ButtonBlock.class)
public abstract class ButtonBlockMixin extends Block {
    public ButtonBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "onEntityCollision")
    private void init3(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        Direction direction = state.get(Properties.HORIZONTAL_FACING);
        int k = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK,pos);
        if (!world.isClient() && k > 0) {
            Vec3d velocity = new Vec3d(direction.getUnitVector()).multiply(k * 0.5);
            entity.addVelocity(velocity.x, velocity.y, velocity.z);
        }
        if (world.isClient() && k > 0 && entity instanceof PlayerEntity player) {//如果有击退附魔
            Vec3d velocity = new Vec3d(direction.getUnitVector()).multiply(k * 0.5);
            player.addVelocity(velocity.x, velocity.y, velocity.z);
        }
    }
}
