package net.mafuyu33.mafishmod.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AfterBlockBreakHandler implements PlayerBlockBreakEvents.After {

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        Hand hand = player.getActiveHand();
        ItemStack itemStack = player.getStackInHand(hand);
        int i = EnchantmentHelper.getLevel(ModEnchantments.NEVER_GONNA,itemStack);//如果手上的物品有被附魔 “你被骗了”
        if(i>0 && (state.isIn(BlockTags.DIAMOND_ORES))){
            PacketByteBuf buf = PacketByteBufs.create();//传输到client端
            ServerPlayNetworking.send((ServerPlayerEntity) player, ModMessages.NEVER_GONNA_ID, buf);
            //检测pos边上是不是有钻石掉落物，有的话替换成煤炭
            startDelayedOperation(world,player,pos);
        }
    }

    public void replaceDiamondsWithCoal(World world, BlockPos pos) {
        // 创建一个立方体区域，以给定坐标为中心，半径为1
        Box box = new Box(pos).expand(1.0);

        // 获取指定区域内的所有 ItemEntity
        List<ItemEntity> itemEntities = world.getEntitiesByType(EntityType.ITEM, box, itemEntity -> {
            // 检查实体持有的物品是否为钻石
            return itemEntity.getStack().getItem() == Items.DIAMOND;
        });

        // 遍历所有 ItemEntity
        for (ItemEntity itemEntity : itemEntities) {
            // 替换成煤炭
            itemEntity.setStack(new ItemStack(Items.COAL));
        }
    }
    private void startDelayedOperation(World world, PlayerEntity player, BlockPos pos) {
        if (world.getServer() != null) {
            world.getServer().execute(() -> {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        replaceDiamondsWithCoal(world,pos);
                    }
                }, 200); // 延迟0.2秒执行，单位为毫秒
            });
        }
    }
}
