package net.jiang.tutorialmod.entity;

import net.jiang.tutorialmod.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.BlockHitResult;

public class TNTProjectileEntity extends ThrownItemEntity {
    public TNTProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public TNTProjectileEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.TNT_PROJECTILE, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.TNT_BALL;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte)3);
            explode();
        }

        this.discard();
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(!this.getWorld().isClient()){
            explode();
        }

        this.discard();
        super.onEntityHit(entityHitResult);
    }

    private void explode() {
        float f = 4.0F;
        this.getWorld().createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), f, World.ExplosionSourceType.TNT);
    }
}