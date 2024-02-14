package net.jiang.tutorialmod.mixin;

import net.jiang.tutorialmod.block.ModBlocks;
import net.jiang.tutorialmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Timer;
import java.util.TimerTask;

@Mixin(ItemEntity.class)
public abstract class KaoFishMixin extends Entity implements Ownable {

    @Unique
    private int counter=0;
    public KaoFishMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract ItemStack getStack();

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo info) {
        World world = this.getWorld();
        BlockPos blockPos = this.getBlockPos();
        FluidState fluidState = world.getFluidState(blockPos);
        if (this.getStack().getItem() == ModItems.RUBY && fluidState.isIn(FluidTags.LAVA)) {
            counter++;
            if(counter>=20) {
                System.out.println(123);
                dropItem(ModItems.RAW_RUBY);
                this.discard();
                counter=0;
            }
        }
    }


}
