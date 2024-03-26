package net.mafuyu33.mafishmod.block.custom;


import net.mafuyu33.mafishmod.entity.TNTProjectileEntity;
import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;


public class PotatoTNTBlock extends SlabBlock {
    public PotatoTNTBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if(entity instanceof LivingEntity) {
            TNTProjectileEntity tntProjectileEntity = new TNTProjectileEntity(((LivingEntity) entity), world);
            tntProjectileEntity.setItem(ModItems.TNT_BALL.getDefaultStack());
            tntProjectileEntity.setVelocity(entity,90,entity.getYaw(), 0.0f, 5f, 0f);
            world.spawnEntity(tntProjectileEntity);
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        super.onDestroyedByExplosion(world, pos, explosion);
    }
}
