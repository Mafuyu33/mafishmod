package net.mafuyu33.mafishmod.mixin.itemmixin;

import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
