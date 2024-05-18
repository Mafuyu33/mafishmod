package net.mafuyu33.mafishmod.mixin.effectmixin.sheep;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.mafuyu33.mafishmod.networking.packet.C2S.SheepBreedingC2SPacket;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow public abstract HungerManager getHungerManager();

    @Unique
    boolean lastStage = false;

    @Unique
    int times = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo info) {
        if(this.getHungerManager().isNotFull() && this.getWorld().isClient){
            times = 0;
            mafishmod$sendC2S();
        }
        if (this.hasStatusEffect(ModStatusEffects.SHEEP_EFFECT) && !this.getHungerManager().isNotFull()) {//变羊药水
            if(this.getWorld().isClient) {
                boolean isSneakKeyPressed = MinecraftClient.getInstance().options.sneakKey.isPressed();
                if (isSneakKeyPressed != lastStage) {//和上次不一样，说明切换了一次
                   times++;
                   mafishmod$sendC2S();
                }
                lastStage = isSneakKeyPressed;
            }else if(SheepBreedingC2SPacket.getTimes()>=10){
                this.getHungerManager().setFoodLevel(this.getHungerManager().getFoodLevel()-3);//减少三点饱食度
                SheepEntity sheepEntity = mafishmod$findSheepAround();
                if(sheepEntity!=null) {
                    System.out.println("createChild!");
                    sheepEntity.breed((ServerWorld) this.getWorld(),sheepEntity);
                }
            }
        }
    }
    @Unique
    private void mafishmod$sendC2S(){
        PacketByteBuf buf = PacketByteBufs.create();//C2S
        buf.writeInt(times);
        ClientPlayNetworking.send(ModMessages.SHEEP_BREEDING_ID, buf);
    }

    @Unique
    private SheepEntity mafishmod$findSheepAround() {
        float distance = 0.8F;

        if (this.getWorld() == null) {
            return null;
        }

        // 获取玩家所在的世界
        World world = this.getWorld();

        // 获取玩家当前位置
        Vec3d playerPos = this.getPos();

        // 定义搜索范围（玩家周围的范围）
        Box searchBox = new Box(
                playerPos.x - distance, playerPos.y - distance, playerPos.z - distance,
                playerPos.x + distance, playerPos.y + distance, playerPos.z + distance
        );

        // 搜索范围内的所有羊实体
        List<SheepEntity> entities = world.getEntitiesByClass(SheepEntity.class, searchBox, entity -> true);

        // 检查是否找到准备繁殖的羊实体
        for (SheepEntity entity : entities) {
            System.out.println("Found Sheep: " + entity);
            if (entity.isInLove()) { // 如果有准备繁殖的羊
                System.out.println("Sheep ready to breed: " + entity);
                return entity;
            }
        }

        System.out.println("No sheep found or ready to breed");
        return null;
    }



}