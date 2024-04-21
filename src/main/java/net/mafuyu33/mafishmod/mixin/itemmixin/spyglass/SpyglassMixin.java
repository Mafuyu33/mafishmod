package net.mafuyu33.mafishmod.mixin.itemmixin.spyglass;

import net.mafuyu33.mafishmod.mixinhelper.BowDashMixinHelper;
import net.mafuyu33.mafishmod.sound.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class SpyglassMixin extends LivingEntity {
	@Shadow public abstract boolean isUsingSpyglass();

	@Shadow public abstract Iterable<ItemStack> getHandItems();

	@Unique
	BlockPos lastHitBlockPos;
	protected SpyglassMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		if(this.isUsingSpyglass() && BowDashMixinHelper.isAttackKeyPressed()){//标记地点
			// 获取玩家的位置和视线方向
			Vec3d playerPos = this.getCameraPosVec(1.0F);
			Vec3d playerLook = this.getRotationVec(1.0F);

			// 设置射线起点和方向
            Vec3d rayEnd = playerPos.add(playerLook.multiply(25565)); // 假设射线长度为 10

			// 进行射线投射
			BlockHitResult blockHitResult = getWorld().raycast(new RaycastContext(playerPos, rayEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this));

			// 检查是否击中方块
			if (blockHitResult.getType() == HitResult.Type.BLOCK) {
				// 获取方块坐标
				BlockPos currentHitBlockPos = blockHitResult.getBlockPos();
				// 如果当前坐标和上一次点击的坐标在 y 轴上相同，则清除存储的坐标值
				if (lastHitBlockPos != null && currentHitBlockPos.getY() == lastHitBlockPos.getY()) {
					lastHitBlockPos = null;
				} else{
					// 否则，存储当前坐标值
					lastHitBlockPos = currentHitBlockPos;
					if(getWorld().isClient) {
						getWorld().playSound((PlayerEntity) (Object) this, this.getBlockPos(), ModSounds.PIN, SoundCategory.PLAYERS);
						System.out.println("击中方块坐标：" + currentHitBlockPos.getX() + ", " + currentHitBlockPos.getY() + ", " + currentHitBlockPos.getZ());
					}
				}

			}


//			sendC2S();
		}
		if(lastHitBlockPos != null){
			// 在hitBlockPos为原点，竖直向上延伸20格生成末地烛粒子
			for (float yOffset = 0; yOffset < 20; yOffset += 0.1f) {
				double particleX = lastHitBlockPos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.1; // 加入随机性模拟波动
				double particleY = lastHitBlockPos.getY() + yOffset + 0.5;
				double particleZ = lastHitBlockPos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.1; // 加入随机性模拟波动

				// 计算粒子密度，可以根据需要调整指数底数和系数
				double density = Math.exp(-yOffset / 20); // 使用指数衰减模拟密度减少

				// 根据粒子密度生成粒子
				if (getWorld().isClient && random.nextFloat() < density) {
					getWorld().addParticle(ParticleTypes.COMPOSTER,true, particleX, particleY, particleZ, 0, 0.05, 0);
				}
			}
		}

		if(this.isHolding(Items.GOAT_HORN) && this.isUsingItem() && lastHitBlockPos!=null){//使用山羊角
			if(!getWorld().isClient) {
				for (int i = 0; i < 20; i++) {
					double xOffset = random.nextDouble() * 16 - 8; // 在-5到5之间生成随机偏移量
					double zOffset = random.nextDouble() * 16 - 8;
					double x = lastHitBlockPos.getX() + 0.5 + xOffset;
					double y = lastHitBlockPos.getY() + 100 + random.nextDouble() * 4 - 2; // 在lastHitBlockPos上空20格附近随机生成Y坐标
					double z = lastHitBlockPos.getZ() + 0.5 + zOffset;

					TntEntity tnt = new TntEntity(EntityType.TNT, getWorld());
					tnt.setPos(x, y, z);

					// 设置不同的向下速度
					double downwardVelocity = random.nextDouble() * 1 - 0.5; // 随机生成一个向下速度，范围在-0.5到0.5之间
					tnt.setVelocity(0, downwardVelocity, 0);

					// 根据向下速度的大小确定引爆时间
					int fuse = (int)(downwardVelocity * -50) + 120; // 根据向下速度计算引爆时间，速度越小，引爆时间越长
					tnt.setFuse(fuse); // 设置TNT的引爆时间

					getWorld().spawnEntity(tnt);
				}
			}
			lastHitBlockPos=null;
		}
	}
//	@Unique
//	public void spawnGiantChicken(ServerWorld world, BlockPos pos) {
//		if ((PlayerEntity)(Object)this instanceof ServerPlayerEntity) {
//
//			MinecraftServer server = world.getServer();
//			// 获取服务器命令调度程序
//			CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
//			try {
//				BlockPos spawnPos = pos.north(20).add(0,20,0);
//
//				// 为鸡添加一个标签以便识别
//				String summonCommand = String.format("summon minecraft:chicken %d %d %d {NoGravity:1b,Rotation:[0F,0F],Motion:[0.0d,0.0d,5d]}", spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
//
//				// 解析指令并获取命令源
//				ParseResults<ServerCommandSource> parseResults
//						= dispatcher.parse(summonCommand, server.getCommandSource());
//
//				// 执行指令
//				dispatcher.execute(parseResults);
//				// 在控制台输出提示信息
//				System.out.println("Spawned a no-AI chicken at " + spawnPos);
//			} catch (CommandSyntaxException e) {
//				// 指令语法异常处理
//				e.printStackTrace();
//			}
//		}
//	}
//	@Unique
//	@Environment(EnvType.CLIENT)
//	private void sendC2S(){
//		PacketByteBuf buf = PacketByteBufs.create();//传输到服务端
//		buf.writeInt(BowDashCoolDown);
//		ClientPlayNetworking.send(ModMessages.Bow_Dash_ID, buf);
//	}
}