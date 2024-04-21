package net.mafuyu33.mafishmod.mixin.itemmixin.SwimmingTripwire;

import net.mafuyu33.mafishmod.mixinhelper.TripwireBlockMixinHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.EntityPose.*;

@Mixin(TripwireBlock.class)
public abstract class TripwireBlockMixin extends Block{
	public TripwireBlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(at = @At("HEAD"), method = "onEntityCollision")
	private void init(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo info) {
		if (!world.isClient && entity.getPose()!=DYING && !entity.isPlayer()) {//绊倒生物
			world.sendEntityStatus(entity, (byte)3);
			entity.setPose(DYING);
		}

		if(entity.getPose()!=SWIMMING && entity.isPlayer()
				&& TripwireBlockMixinHelper.getEntityValue(entity.getId())<=0){//绊倒玩家
			if(!world.isClient) {
				Vec3d velocity = entity.getVelocity(); // 获取实体的速度向量
				System.out.println(velocity);
				if (Math.abs(velocity.y) > 0.07) {
//					world.sendEntityStatus(entity, (byte) 3);
					TripwireBlockMixinHelper.storeEntityValue(entity.getId(), 50);
				}
			}else {
				entity.addVelocity(0, 0.3, 0);
			}
		}
	}
}