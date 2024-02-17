package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.mixinhelper.WeaponEnchantmentMixinHelper;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow protected abstract void drop(DamageSource source);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        super.tick();
        if(this.getType()==EntityType.VILLAGER) {
            int entityId = this.getId();// 获取实体的ID
            int times = WeaponEnchantmentMixinHelper.getEntityValue(entityId);
            if (times > 0) {
                WeaponEnchantmentMixinHelper.storeEntityValue(entityId, times - 1);
                this.dropItem(Items.EMERALD);
            }
        }
    }
}
