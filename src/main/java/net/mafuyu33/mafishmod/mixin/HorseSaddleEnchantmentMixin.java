package net.mafuyu33.mafishmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(AbstractHorseEntity.class)
public abstract class HorseSaddleEnchantmentMixin extends AnimalEntity implements InventoryChangedListener, RideableInventory, Tameable, JumpingMount, Saddleable {
    protected HorseSaddleEnchantmentMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }
    @Shadow
    protected SimpleInventory items;

    @Inject(at = @At("HEAD"), method = "tick")
    private void init1(CallbackInfo info) {

        LivingEntity controllingPassenger = this.getControllingPassenger();//骑乘者
        ItemStack saddledItem = this.items.getStack(0);
        int k = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, saddledItem);
        int j = EnchantmentHelper.getLevel(Enchantments.CHANNELING, saddledItem);

        if(k>0 && controllingPassenger!=null) {//火焰附加的鞍
            controllingPassenger.setOnFireFor(1);
        }
        if(j>0 && controllingPassenger!=null){//引雷的鞍
            BlockPos blockPos = controllingPassenger.getBlockPos();
            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
            if (lightningEntity != null) {
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                this.getWorld().spawnEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                this.playSound(soundEvent, 5, 1.0F);
            }
        }
    }
}
