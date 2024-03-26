package net.mafuyu33.mafishmod.entity;

import net.mafuyu33.mafishmod.item.ModItems;
import net.mafuyu33.mafishmod.render.CustomParticleRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LightningProjectileEntity extends ThrownItemEntity {
    public LightningProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public LightningProjectileEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.LIGHTNING_PROJECTILE, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.LIGHTNING_BALL;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockPos = this.getBlockPos();
        lightning(blockPos);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte)3);
            lightning(blockHitResult.getBlockPos());
            CustomParticleRenderer.spawnFlameParticles(blockHitResult.getPos());
        }

        this.discard();
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(!this.getWorld().isClient()){
            lightning(entityHitResult.getEntity().getBlockPos());
            CustomParticleRenderer.spawnFlameParticles(entityHitResult.getPos());
        }

        this.discard();
        super.onEntityHit(entityHitResult);
    }

    private void lightning(BlockPos blockPos) {
        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
        if (lightningEntity != null) {
            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
            this.getWorld().spawnEntity(lightningEntity);
            SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
            this.playSound(soundEvent, 5, 1.0F);
        }

    }
}