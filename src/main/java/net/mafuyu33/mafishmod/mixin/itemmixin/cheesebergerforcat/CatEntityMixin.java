package net.mafuyu33.mafishmod.mixin.itemmixin.cheesebergerforcat;

import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity {
    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "initGoals")
    private void init(CallbackInfo info) {
        this.goalSelector.add(4,new CatEntity.TemptGoal((CatEntity)(Object)this, 0.6, Ingredient.ofItems(ModItems.CHEESE_BERGER), false));
    }
}
