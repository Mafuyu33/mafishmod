package net.mafuyu33.mafishmod.item.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mafuyu33.mafishmod.VRPlugin;
import net.mafuyu33.mafishmod.util.VRDataHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TimeStopItem extends Item {
    public TimeStopItem(Settings settings) {
        super(settings);
    }
    private static boolean startStop = false;
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient){
        startStop=!startStop;


            System.out.println("切换");
            if (startStop) {
                if (user instanceof ServerPlayerEntity) {
                    MinecraftServer server = user.getServer();
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
                        user.sendMessage(Text.literal(("启动时间停止")),true);
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("tick freeze", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                }
            }else {
                MinecraftServer server = user.getServer();
                // 获取服务器命令调度程序
                CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
                try {
                    // 解析指令并获取命令源
                    user.sendMessage(Text.literal(("停止时间停止")),true);
                    ParseResults<ServerCommandSource> parseResults
                            = dispatcher.parse("tick unfreeze", server.getCommandSource());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            }
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    private static Vec3d lastPos= new Vec3d(0, 0, 0);
    private static Vec3d lastPosMainController= new Vec3d(0, 0, 0);
    private static Vec3d lastPosOffController= new Vec3d(0, 0, 0);
    private static Vec3d lastPosHMD= new Vec3d(0, 0, 0);
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (startStop && entity instanceof PlayerEntity user && !world.isClient
                && VRPlugin.canRetrieveData(user)) {//有MC-VR-API并且在VR中的时候
            Vec3d currentPosMainController = VRDataHandler.getControllerPosition(user, 0);
            Vec3d currentPosOffController = VRDataHandler.getControllerPosition(user, 1);
            Vec3d currentPosHMD = VRDataHandler.getHMDPosition(user);

            double mainControllerDistance = currentPosMainController.distanceTo(lastPosMainController); // 计算当前位置和上一个位置之间的距离
            double offControllerDistance = currentPosOffController.distanceTo(lastPosOffController);
            double HMDDistance = currentPosHMD.distanceTo(lastPosHMD);

            System.out.println("main"+mainControllerDistance);
            System.out.println("off"+offControllerDistance);
            System.out.println("HMD"+HMDDistance);

            boolean isMoving = mainControllerDistance > 0.051
                    || offControllerDistance > 0.051
                    || HMDDistance > 0.051;// 设置阈值

            //你帮我写判断语句的部分...
            CommandProcessing(user,isMoving);

            lastPosMainController=currentPosMainController;
            lastPosOffController=currentPosOffController;
            lastPosHMD=currentPosHMD;

        }else if(!world.isClient && entity instanceof PlayerEntity user && startStop) {//普通版本
            Vec3d currentPos = user.getPos();
            double distance = currentPos.distanceTo(lastPos); // 计算当前位置和上一个位置之间的距离
            boolean isMoving = distance > 0.09; // 设置一个小于的阈值，比如0.1

            lastPos = currentPos;

//            System.out.println(distance);

            CommandProcessing(user,isMoving);
        }
    }
    private static void CommandProcessing(PlayerEntity user,boolean isMoving){
        if (isMoving) {
            MinecraftServer server = user.getServer();
            // 获取服务器命令调度程序
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
            try {
                // 解析指令并获取命令源
                ParseResults<ServerCommandSource> parseResults
                        = dispatcher.parse("tick unfreeze", server.getCommandSource());
                // 执行指令
                dispatcher.execute(parseResults);

                // 在控制台输出提示信息
            } catch (CommandSyntaxException e) {
                // 指令语法异常处理
                e.printStackTrace();
            }
        } else {
            MinecraftServer server = user.getServer();
            // 获取服务器命令调度程序
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
            try {
                // 解析指令并获取命令源
                ParseResults<ServerCommandSource> parseResults
                        = dispatcher.parse("tick freeze", server.getCommandSource());
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
