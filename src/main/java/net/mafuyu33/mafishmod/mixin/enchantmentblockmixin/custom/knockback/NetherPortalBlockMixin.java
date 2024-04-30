package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.knockback;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.NetherPortalBlock;
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

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin extends Block {
    public NetherPortalBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "onEntityCollision")
    private void init3(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        int knockbackLevel = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK, pos);
        if (knockbackLevel > 0) {
            Vec3d entityPos = entity.getPos();
            Vec3d blockPosVec = Vec3d.ofCenter(pos);

            // 计算从方块指向实体的向量
            Vec3d directionToEntity = entityPos.subtract(blockPosVec);

            // 归一化向量并乘以击退等级
            Vec3d knockbackVector = directionToEntity.normalize().multiply(-knockbackLevel * 0.5);

            // 应用击退力
            if (!world.isClient()) {
                entity.addVelocity(knockbackVector.x, knockbackVector.y, knockbackVector.z);
            }

            if (world.isClient() && entity instanceof PlayerEntity player) {
                player.addVelocity(knockbackVector.x, knockbackVector.y, knockbackVector.z);
            }
        }
    }

}
