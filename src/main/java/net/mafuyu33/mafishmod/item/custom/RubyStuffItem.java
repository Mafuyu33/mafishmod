package net.mafuyu33.mafishmod.item.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.util.NbtType;
import net.mafuyu33.mafishmod.effect.ModStatusEffects;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class RubyStuffItem extends Item {
    public RubyStuffItem(Settings settings) {
        super(settings);
    }
    int timer = 0; //计时器
    private boolean startGoing=false;
    private boolean finishGoing=false;
    private BlockPos firstPos;
    private BlockPos finalPos;
    private GameMode gameMode = GameMode.SURVIVAL;

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
                EnchantmentHelper.getLevel(ModEnchantments.GO_TO_SKY, offHandStack) :
                offHandStack.getItem() == this ?
                        EnchantmentHelper.getLevel(ModEnchantments.GO_TO_SKY, mainHandStack) :
                        0;//检测除了法杖的那只手的附魔
            System.out.println(k);//一直是0
            if ((offHandStack.getItem() == Items.WRITTEN_BOOK || mainHandStack.getItem()==Items.WRITTEN_BOOK)) {

                if (k > 0 && checkForSkywardPortal(user).found) {//通天术附魔
                    if (user instanceof ServerPlayerEntity) {
                        firstPos = user.getBlockPos();
                        gameMode = getPlayerGameMode(((ServerPlayerEntity) user));
                        MinecraftServer server = user.getServer();
                        // 获取服务器命令调度程序
                        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
                        try {
                            // 解析指令并获取命令源
                            ParseResults<ServerCommandSource> parseResults
                                    = dispatcher.parse("gamemode spectator @p", server.getCommandSource());
                            // 执行指令
                            dispatcher.execute(parseResults);

                            // 在控制台输出提示信息
                        } catch (CommandSyntaxException e) {
                            // 指令语法异常处理
                            e.printStackTrace();
                        }

                        // 获取落脚方块的位置
                        finalPos = checkForSkywardPortal(user).landingPos;

                        // 计算玩家到落脚方块的距离
                        double distance = user.getPos().distanceTo(Vec3d.ofCenter(finalPos));

                        startGoing = true;


                    }
                } else if (k > 0 && !checkForSkywardPortal(user).found) {
                    user.sendMessage(Text.literal((String.valueOf("没有合适的落脚方块释放通天术"))), true);
                }





                //更多的附魔。。。




            }



        if(mainHandStack.getItem()==this){
            itemStack = mainHandStack;
        }else {
            itemStack = offHandStack;
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }


    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        if(!world.isClient()) {


            ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
            NbtCompound nbt = stack.getOrCreateNbt();

            // 添加书籍的内容
            addPageContent(bookStack, "§n你好陌生人，愿意和我签订契约，成为魔法少女吗？\n " +
                    "§r§kMafuyu33Mafuyu33\n" +
                    "§r§l使用方法：就是这样喵\n" +
                    "§c在第一页签下你的名字。\n" +
                    "§r§0给此书添加对应附魔，一手法杖一手魔法书心中默念咒语即可释放对应魔法\n" +
                    "\n" +
                    "现已收录的魔法：\n" +
                    "1-通天术"+
                    "2-时间停止（伪）");
            addPageContent(bookStack, "通天术：\n" +
                    "似乎是海拉鲁大陆的失传魔法,如果玩家正上方的一定距离内有可穿透物体，就可以施展通天术。\n" +
                    "注意事项：施展通天术的时候请不要左右移动，否则后果自负。");

            player.getInventory().insertStack(bookStack);
        }
    }

    // 添加内容到书的页面中
    public static void addPageContent(ItemStack stack, String content) {
        // 获取书的NBT数据
        NbtCompound nbt = stack.getOrCreateNbt();

        // 获取书的页面列表
        NbtList pagesList = nbt.getList("pages", NbtType.STRING);

        // 将内容添加到页面列表中
        pagesList.add(NbtString.of(content));

        // 更新书的NBT数据中的页面列表
        nbt.put("pages", pagesList);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof ServerPlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (startGoing) {
                //每个tick向上移动一段距离
                System.out.println("开始移动");
                player.sendMessage(Text.literal((String.valueOf("正在释放通天术"))),true);

                //检查是否到达落脚方块上方
                if (finalPos.getY()<player.getBlockPos().getY()) {// 如果到了落脚方块，则停止
                    startGoing=false;
                    finishGoing=true;
                }
                if (player.getBlockPos().getY()<firstPos.getY()-1.5){
                    player.teleport(firstPos.getX(),firstPos.getY(),firstPos.getZ());
                }
            }
        if (finishGoing) {
            System.out.println("停止移动");
            player.sendMessage(Text.literal((String.valueOf("通天术释放结束"))),true);
            MinecraftServer server = player.getServer();
            // 获取服务器命令调度程序
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
            if (gameMode == GameMode.SURVIVAL) {
                try {
                    // 解析指令并获取命令源
                    ParseResults<ServerCommandSource> parseResults
                            = dispatcher.parse("gamemode survival @p", server.getCommandSource());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            } else if (gameMode == GameMode.CREATIVE) {
                try {
                    // 解析指令并获取命令源
                    ParseResults<ServerCommandSource> parseResults
                            = dispatcher.parse("gamemode creative @p", server.getCommandSource());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            } else if (gameMode == GameMode.ADVENTURE) {
                try {
                    // 解析指令并获取命令源
                    ParseResults<ServerCommandSource> parseResults
                            = dispatcher.parse("gamemode adventure @p", server.getCommandSource());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            }
            finishGoing=false;
        }
    }
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
        tooltip.add(Text.translatable("tooltip.mafishmod.ruby_stuff.tooltip"));
        super.appendTooltip(stack, world, tooltip, context);
    }

    // 清除特定的药水状态
    public void clearStatusEffect(PlayerEntity player, StatusEffect statusEffect) {
            player.removeStatusEffect(statusEffect);
    }

    public static GameMode getPlayerGameMode(ServerPlayerEntity player) {
            return player.interactionManager.getGameMode();
    }


    public static class CheckResult {
        private boolean found; // 是否找到符合条件的方块
        private BlockPos landingPos; // 落脚方块的位置

        // 构造函数
        public CheckResult(boolean found, BlockPos landingPos) {
            this.found = found;
            this.landingPos = landingPos;
        }
    }


    // 检测是否有连续空气方块和落脚方块的方法
    public static CheckResult checkForSkywardPortal(PlayerEntity player) {
        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();

        // 检查头顶 30 格内的方块
        for (int offsetY = 3; offsetY <= 50; offsetY++) {
            BlockPos checkPos = playerPos.up(offsetY);
            BlockPos checkPosAbove = checkPos.up();

            // 检查当前方块和上方方块是否符合条件
            if (world.isAir(checkPos) && world.isAir(checkPosAbove)
                    &&(!world.isAir(checkPos.down()))) {
                System.out.println("找到符合条件的方块");
                return new CheckResult(true, checkPos.down()); // 找到符合条件的方块，返回 true
            }
        }
        System.out.println("未找到符合条件的方块");
        return new CheckResult(false, null); // 未找到符合条件的方块，返回 false
    }
    
}
