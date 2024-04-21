package net.mafuyu33.mafishmod.mixin.itemmixin;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.mafuyu33.mafishmod.mixinhelper.BowDashMixinHelper;
import net.mafuyu33.mafishmod.mixinhelper.TripwireBlockMixinHelper;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.mafuyu33.mafishmod.sound.ModSounds;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public abstract class BowDashMixin extends LivingEntity {
    @Shadow public abstract void tick();

    @Unique
    int BowDashCoolDown = 0;
    protected BowDashMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);

    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void init1(CallbackInfo ci) {

        if(getWorld().isClient && this.isHolding(Items.BOW) && this.isUsingItem()
                && BowDashMixinHelper.isAttackKeyPressed() && BowDashCoolDown<=0){//弓箭手突击
            // 获取玩家的水平朝向角度（角度值）
            Vec3d velocity = this.getVelocity();
            // 投影到水平平面上
            Vec3d horizontalMotion = new Vec3d(velocity.x, 0, velocity.z);
            // 将水平移动向量标准化
            if (horizontalMotion.lengthSquared() > 0) {
                horizontalMotion = horizontalMotion.normalize();
            }
            float amp = 2;
            this.addVelocity(amp*horizontalMotion.x,0.14,amp*horizontalMotion.z);

            sendC2S();

            System.out.println("突进");

            getWorld().playSound(this,this.getBlockPos(),
                    ModSounds.DASH_SOUND, SoundCategory.PLAYERS,1f,1f);
            BowDashCoolDown=20;

        }
        if(getWorld().isClient && BowDashCoolDown>0){//弓箭手突击内置冷却部分，传递数据包到服务端
            BowDashCoolDown--;
//            System.out.println(BowDashCoolDown);
            sendC2S();
        }


        if (!getWorld().isClient && BowDashMixinHelper.getHitCoolDown(this.getId())!=0) {//时间变慢部分
            MinecraftServer server = this.getServer();
            if (server != null) {
                if(BowDashMixinHelper.getHitCoolDown(this.getId())>=12){
                    // 获取服务器命令调度程序
                    CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
                    try {
                        // 解析指令并获取命令源
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("gamerule sendCommandFeedback false", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                    try {
                        // 解析指令并获取命令源
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("tick rate 6", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                    try {
                        // 解析指令并获取命令源
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("effect give @p minecraft:slowness infinite 3 true", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                    try {
                        // 解析指令并获取命令源
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("effect give @p minecraft:night_vision infinite 0 true", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                }else {
                    // 获取服务器命令调度程序
                    CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
                    try {
                        // 解析指令并获取命令源
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("tick rate 20", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                    try {
                        // 解析指令并获取命令源
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("effect clear @p minecraft:slowness", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                    try {
                        // 解析指令并获取命令源
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("effect clear @p minecraft:night_vision", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @Unique
    @Environment(EnvType.CLIENT)
    private void sendC2S(){
        PacketByteBuf buf = PacketByteBufs.create();//传输到服务端
        buf.writeInt(BowDashCoolDown);
        ClientPlayNetworking.send(ModMessages.Bow_Dash_ID, buf);
    }
}
