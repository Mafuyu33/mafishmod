package net.jiang.tutorialmod.item.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.jiang.tutorialmod.effect.ModStatusEffects;
import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.jiang.tutorialmod.networking.ModMessages;
import net.jiang.tutorialmod.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.net.Proxy;
import java.util.List;
import java.util.Objects;

public class RubyStuffItem extends Item {
    public RubyStuffItem(Settings settings) {
        super(settings);
    }
    int timer =0; //计时器

//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//
//
//        return super.use(world, user, hand);
//    }
//
//    @Override
//    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
//        super.usageTick(world, user, stack, remainingUseTicks);
//        timer++;
//        if(timer<30){
//            SoundEvent soundEvent = ModSounds.SOUND_BLOCK_HIT;
//
//            user.playSound(soundEvent, 0.5f, 1f);//触发声音
//            System.out.println("蓄力中");
//        }
//        //播放蓄力音效
//    }
//
//    @Override
//    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
//        super.onStoppedUsing(stack, world, user, remainingUseTicks);
//        if(timer>30){
//            //释放法术
//
//
//            //播放声音
//            SoundEvent soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE.value();
//            user.playSound(soundEvent, 1f, 1f);//触发声音
//            timer=0;//清零
//            System.out.println("发射啦！");
//        }
//    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack mainHandStack = user.getMainHandStack();
        ItemStack offHandStack = user.getOffHandStack();
        ItemStack itemStack;

        int k = mainHandStack.getItem() == this ?
                EnchantmentHelper.getLevel(ModEnchantments.EIGHT_GODS_PASS_SEA, offHandStack) :
                offHandStack.getItem() == this ?
                        EnchantmentHelper.getLevel(ModEnchantments.EIGHT_GODS_PASS_SEA, mainHandStack) :
                        0;//检测除了法杖的那只手的附魔
            System.out.println(k);//一直是0
            if (k > 0) {
                if (user instanceof ServerPlayerEntity) {
                    System.out.println("成功检测到玩家");
                    ClientPlayNetworking.send(ModMessages.EXAMPLE_ID, PacketByteBufs.create());
//                    EntityType.COW.spawn(((ServerWorld) user.getWorld()),user.getBlockPos(), SpawnReason.TRIGGERED);
                    NbtCompound nbt = new NbtCompound();
                    nbt.putInt("playerGameType", GameMode.SPECTATOR.getId()); // 观察者模式的整数值为 3
                    ((ServerPlayerEntity) user).setGameMode(nbt);
                }
            }



        if(mainHandStack.getItem()==this){
            itemStack = mainHandStack;
        }else {
            itemStack = offHandStack;
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        int k = EnchantmentHelper.getLevel(Enchantments.CHANNELING, itemStack);
        if (k > 0) {//引雷
            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(context.getWorld());
            if (lightningEntity != null) {
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                lightningEntity.setChanneler(playerEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity) playerEntity : null);
                context.getWorld().spawnEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                if(playerEntity !=null) {
                    playerEntity.playSound(soundEvent, 5, 1.0F);
                }
            }
        }


    if(playerEntity!=null && playerEntity.hasStatusEffect(ModStatusEffects.FLOWER_EFFECT)) {
        int j = (playerEntity.getStatusEffect(ModStatusEffects.FLOWER_EFFECT)).getAmplifier()+1;
        int radius = 2; // 设置半径，可以根据需要调整
        BlockState flowerState = Blocks.BLUE_ORCHID.getDefaultState();

        for (int xOffset = -radius; xOffset <= radius; xOffset++) {
            for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                BlockPos flowerPos = blockPos.add(xOffset, 1, zOffset); // 在Y轴加1
                world.setBlockState(flowerPos, flowerState, 3); // 在flowerPos处生成花
            }
        }
        if(j>0) {
            clearStatusEffect(playerEntity, ModStatusEffects.FLOWER_EFFECT);
            playerEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.FLOWER_EFFECT, 3600, j-2));
        }else {
            clearStatusEffect(playerEntity, ModStatusEffects.FLOWER_EFFECT);
        }
    }






        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.tutorialmod.ruby_stuff.tooltip"));
        super.appendTooltip(stack, world, tooltip, context);
    }

    // 清除特定的药水状态
    public void clearStatusEffect(PlayerEntity player, StatusEffect statusEffect) {
            player.removeStatusEffect(statusEffect);
    }
}
