package net.mafuyu33.mafishmod.mixin.worldtick;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mafuyu33.mafishmod.mixinhelper.BellBlockDelayMixinHelper;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Mixin(MinecraftServer.class)
public abstract class ServerTickMixin {
	@Shadow private PlayerManager playerManager;

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		//获取存储BellBlockEntity的所有键的集合
		Set<BlockPos> bellBlockEntityKeys = BellBlockDelayMixinHelper.BellBlockEntityMap.keySet();
		// 遍历集合并执行操作
		for (BlockPos blockPos : bellBlockEntityKeys) {
			// 根据键从Map中获取对应的值
			BellBlockEntity bellBlockEntity = BellBlockDelayMixinHelper.getBellBlockEntity(blockPos);
			Direction direction = BellBlockDelayMixinHelper.getDirection(blockPos);
			int hitCoolDown = BellBlockDelayMixinHelper.getHitCoolDown(blockPos);

			if (hitCoolDown<20 && hitCoolDown>=0) {
				BellBlockDelayMixinHelper.storeHitCoolDown(blockPos,hitCoolDown+1);
			}

			if (hitCoolDown >= 20) {
				bellBlockEntity.activate(direction);
				PacketByteBuf buf = PacketByteBufs.create();//S2C
				buf.writeInt(1);
				buf.writeBlockPos(blockPos);
				String[] playerNames = playerManager.getPlayerNames();
				PlayerEntity[] players = new PlayerEntity[playerNames.length];

				for (int i = 0; i < playerNames.length; i++) {
					PlayerEntity player = playerManager.getPlayer(playerNames[i]);
					players[i] = player;
				}
				for (PlayerEntity player : players) {
					ServerPlayNetworking.send((ServerPlayerEntity) player, ModMessages.BELL_SOUND_ID, buf);
				}
				BellBlockDelayMixinHelper.HitCoolDownMap.remove(blockPos);
				BellBlockDelayMixinHelper.DirectionMap.remove(blockPos);
				BellBlockDelayMixinHelper.BellBlockEntityMap.remove(blockPos);
			}

		}


	}
}