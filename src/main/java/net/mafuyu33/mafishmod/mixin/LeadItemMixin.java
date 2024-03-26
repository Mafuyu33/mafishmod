package net.mafuyu33.mafishmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LeadItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LeadItem.class)
public class LeadItemMixin extends Item {


	public LeadItemMixin(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (entity instanceof MobEntity) {
			MobEntity target = (MobEntity) entity;
			return attachHeldMobsToMob(user, target);
		}
		return ActionResult.PASS;
	}

	@Unique
	private static ActionResult attachHeldMobsToMob(PlayerEntity player, MobEntity target) {
		World world = target.getWorld();
		BlockPos targetPos = target.getBlockPos();
		LeashKnotEntity leashKnotEntity = LeashKnotEntity.getOrCreate(world, targetPos);
		leashKnotEntity.onPlace();

		target.attachLeash(leashKnotEntity, true);

		world.emitGameEvent(GameEvent.ENTITY_PLACE, targetPos, GameEvent.Emitter.of(player));

		return ActionResult.SUCCESS;
	}
}