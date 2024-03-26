package net.mafuyu33.mafishmod.entity;

import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class StoneBallProjectileEntity extends ThrownItemEntity {
    public StoneBallProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public StoneBallProjectileEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.STONE_PROJECTILE, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.STONE_BALL;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }


//    @Override
//    protected void onBlockHit(BlockHitResult blockHitResult) {
//        if(!this.getWorld().isClient()) {
//            this.getWorld().sendEntityStatus(this, (byte)3);
//
//        }
//
//        this.discard();
//        super.onBlockHit(blockHitResult);
//    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            entity.damage(this.getDamageSources().thrown(this, entity2), 6.0F);
            if (entity2 instanceof LivingEntity) {
                this.applyDamageEffects((LivingEntity) entity2, entity);
            }
            this.discard(); // 雪球命中后销毁
        }
    }
}