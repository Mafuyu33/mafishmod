package net.mafuyu33.mafishmod.entity;

import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DiamondProjectileEntity extends ThrownItemEntity {
    public DiamondProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public DiamondProjectileEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.DIAMOND_PROJECTILE, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.STONE_BALL;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }


    private int bounceCount = 0;
    private long lastBounceTime = 0; // 用于记录上一次增加计数的时间
    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();
        BlockPos blockPos = this.getBlockPos();
        FluidState fluidState = world.getFluidState(blockPos);
        long currentTime = System.currentTimeMillis(); // 获取当前时间

        boolean isInWater = false;
        if (fluidState.isIn(FluidTags.WATER)) {
            isInWater = true;// 检测是否在水中
            // 计算时间间隔并更新上一次弹跳的时间
            long timeSinceLastBounce = currentTime - lastBounceTime;

            if(timeSinceLastBounce>200){
                bounceCount++;
                this.setVelocity(this.getVelocity().multiply(1, -1, 1));
                if (getOwner()!=null) {
                    getOwner().sendMessage(Text.literal((String.valueOf(bounceCount+"次"))));
                }
            }
            lastBounceTime = currentTime;

        } else {
            isInWater = false;
        }

        // 以一定速度递减，模拟速度减慢的效果
        if (isInWater) {
            this.setVelocity(this.getVelocity().multiply(0.95, 1, 0.95));
        }


        Vec3d velocity = this.getVelocity(); //获取速度向量
        double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z); // 计算水平速度
        // 当速度足够小，或者y轴速度太大，并且在水里掉落钻石
        if (bounceCount!=0 & (horizontalSpeed < 0.095 || this.getVelocity().y > 0.5)) {
            dropItem(Items.DIAMOND);
            this.discard();
            bounceCount = 0;
            if (getOwner()!=null) {
                getOwner().sendMessage(Text.literal((String.valueOf("沉了(╯‵□′)╯"))));
            }
        }
    }
//    public double calculateDecay(int bounceCount) {
//        // 设置抛物线的最高点和递减速度
//        double maxHeight = 0.7; // 最高点高度
//        double decayRate = 0.03; // 递减速度
//
//        // 计算递减值
//        double decay = maxHeight - (bounceCount * decayRate);
//
//        // 确保递减值不小于0
//        if (decay < 0) {
//            decay = 0;
//        }
//
//        return decay;
//    }
//    // 添加一个方法来获取弹跳次数
//    public int getBounceCount() {
//        return bounceCount;
//    }


    //    @Override
//    public void tick() {
//        super.tick();
//        World world = this.getWorld();
//        BlockPos blockPos = this.getBlockPos();
//        FluidState fluidState = world.getFluidState(blockPos);
//        if (fluidState.isIn(FluidTags.WATER)) {
//            this.addVelocity(0, 1, 0);
//        }
//    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        dropItem(Items.DIAMOND);
        this.discard();
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
            entityHitResult.getEntity().damage(getDamageSources().
                    thrown(entityHitResult.getEntity(),getOwner()),10);
            dropItem(Items.DIAMOND);
            this.discard();
        }
    }
