package net.mafuyu33.mafishmod.item.vrcustom;

import net.mafuyu33.mafishmod.VRPlugin;
import net.mafuyu33.mafishmod.util.VRDataHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VrGettingOverItItem extends Item {
    public VrGettingOverItItem(Settings settings) {
        super(settings);
    }
    Vec3d extendPosition;

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(entity instanceof PlayerEntity player){
            if(player.isHolding(stack.getItem())){
                if (VRPlugin.canRetrieveData(player)) {//vr
                    //禁止玩家使用方向键移动


                    //生成锤头的位置
                    Vec3d mainPos = VRDataHandler.getMainhandControllerPosition(player);
                    Vec3d offPos = VRDataHandler.getOffhandControllerPosition(player);
                    // 获取玩家当前活跃的手
                    ItemStack mainHandStack = player.getMainHandStack();

                    // 判断活跃的手并计算扩展位置
                    if (mainHandStack == stack) {
                        // 如果活跃的是右手，从mainPos向offPos扩展
                        extendPosition = extendPosition(offPos, mainPos, 2.0);
                    } else {
                        // 如果活跃的是左手，从offPos向mainPos扩展
                        extendPosition = extendPosition(mainPos, offPos, 2.0);
                    }
                    world.addParticle(ParticleTypes.BUBBLE,extendPosition.x,extendPosition.y,extendPosition.z,0,0,0);

                   /*
                    物理碰撞部分，当锤头在方块内部的时候，判定为卡住（攀爬爪代码），并且把他的位置限制在方块的最外面（可以用掉落物被推出方块的代码）
                    此时以锤头的位置作为基准，与玩家两个手柄的位置的变化来进行计算，来改变玩家的位置（而且要有惯性，所以不能单纯
                    地通过改变坐标位置来实现，而要给玩家施加速度的方式，所以还要储存上一次的位置来计算双手的速度）。

                    还要有一个锤头能拿出来的判定，通过检测两只手的变化，如果判定到锤头往方块外面的方向移动了就切换状态，判定为没卡住，让锤头可以自由移动。
                    */
                    if(isInsideSolidBlock(world,extendPosition)){
                        player.sendMessage(Text.literal("Inside Block!"), true);
                    }
                }else {
                    player.sendMessage(Text.literal("sorry, this item currently only working with VR Mode :("), true);
                }
            }
        }
    }
    public Vec3d extendPosition(Vec3d mainPos, Vec3d offPos, double distance) {
        // 从mainPos到offPos的方向向量
        Vec3d direction = new Vec3d(offPos.x - mainPos.x, offPos.y - mainPos.y, offPos.z - mainPos.z);

        // 计算方向向量的长度
        double length = Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z);

        // 标准化方向向量
        Vec3d normalizedDirection = new Vec3d(direction.x / length, direction.y / length, direction.z / length);

        // 将标准化的方向向量乘以所需延伸的距离
        Vec3d extendedVector = new Vec3d(normalizedDirection.x * distance, normalizedDirection.y * distance, normalizedDirection.z * distance);

        // 将延伸向量加到mainPos上，得到新的坐标位置
        return new Vec3d(mainPos.x + extendedVector.x, mainPos.y + extendedVector.y, mainPos.z + extendedVector.z);
    }

    public static boolean isInsideSolidBlock(World world, Vec3d position) {
        int x = (int) Math.floor(position.x);
        int y = (int) Math.floor(position.y);
        int z = (int) Math.floor(position.z);
        // 将 Vec3d 坐标转换为 BlockPos
        BlockPos blockPos = new BlockPos(x, y, z);

        // 获取给定位置的方块状态
        BlockState blockState = world.getBlockState(blockPos);

        // 检查方块是否为固体
        return blockState.isSolidBlock(world, blockPos);
    }
}
