package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.punchtrapdoor;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin {
    @Inject(at = @At("HEAD"), method = "playToggleSound")
    private void init(PlayerEntity player, World world, BlockPos pos, boolean _open, CallbackInfo ci) {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.get(TrapdoorBlock.FACING);
        Vec3d directionVector  = mafishmod$get45DegreeVector(facing).normalize();
        List<Entity> entities =  getEntitiesOnBlockPos(world,pos);

        int k = BlockEnchantmentStorage.getLevel(Enchantments.PUNCH,pos);
        System.out.println(entities);
        if (k > 0 && entities!=null && _open) {//如果有冲击附魔,并且在活板门上，并且活板门打开
            for (Entity entity : entities) {
                entity.addVelocity(directionVector.x * k, directionVector.y * k, directionVector.z * k);
            }
        }
    }


    @Unique
    public Vec3d mafishmod$get45DegreeVector(Direction facing) {
        Vec3d directionVector;

        switch (facing) {
            case NORTH:
                directionVector = new Vec3d(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
                break;
            case SOUTH:
                directionVector = new Vec3d(0, Math.sqrt(2)/2, -Math.sqrt(2)/2);
                break;
            case WEST:
                directionVector = new Vec3d(Math.sqrt(2)/2, Math.sqrt(2)/2, 0);
                break;
            case EAST:
                directionVector = new Vec3d(-Math.sqrt(2)/2, Math.sqrt(2)/2, 0);
                break;
            default:
                directionVector = Vec3d.ZERO;
                break;
        }

        return directionVector;
    }

    @Unique
    private List<Entity> getEntitiesOnBlockPos(World world, BlockPos blockPos) {
        // 定义稍微扩展的边界框以覆盖方块的表面区域
        Box boundingBox = new Box(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1);

        // 获取边界框内的所有实体
        List<Entity> entities = world.getEntitiesByClass(Entity.class, boundingBox, entity -> {
            // 检查实体是否站在指定的方块上
            BlockPos entityPos = entity.getSteppingPos();
            return entityPos.equals(blockPos);
        });

        return entities;
    }
}
