package net.mafuyu33.mafishmod.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.mafuyu33.mafishmod.item.ModItems;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

public class FuProjectileEntity extends ThrownItemEntity {
    public FuProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public FuProjectileEntity(LivingEntity livingEntity, World world) {
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



    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        World world = getWorld();
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        boolean isWoodenBlock = blockState.isIn(BlockTags.LOGS);

        if(!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte)3);
            if(isWoodenBlock){
                world.removeBlock(blockPos, false);
                dropItem(blockState.getBlock());
            }
            ItemStack itemStack = this.getItem();
            Entity owner = this.getOwner();
            if(owner!=null) {
                SendC2S(owner.getUuid());//设置玩家
            }

            int i = EnchantmentHelper.getLevel(Enchantments.LOYALTY,this.getStack());
            if(owner!=null && i>0){
                ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), itemStack);
                double d = owner.getX() - this.getX();
                double e = owner.getY() - this.getY();
                double f = owner.getZ() - this.getZ();
                itemEntity.setVelocity(d * 0.1, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08, f * 0.1);
                this.getWorld().spawnEntity(itemEntity);
            }else {
                dropStack(itemStack);
            }
        }

        this.discard();
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            entity.damage(this.getDamageSources().thrown(this, entity2), 4F);
            if (entity2 instanceof LivingEntity) {
                this.applyDamageEffects((LivingEntity) entity2, entity);
            }
            ItemStack itemStack = this.getItem();
            itemStack.setHolder(this.getOwner());//设置玩家
            dropStack(this.getItem());
            this.discard(); // 命中后销毁
        }
    }


    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Environment(EnvType.CLIENT)
    private void SendC2S(UUID uuid){
        PacketByteBuf buf = PacketByteBufs.create();//C2S
        buf.writeInt(2);
        buf.writeUuid(uuid);
        ClientPlayNetworking.send(ModMessages.FU_ID, buf);
    }
}