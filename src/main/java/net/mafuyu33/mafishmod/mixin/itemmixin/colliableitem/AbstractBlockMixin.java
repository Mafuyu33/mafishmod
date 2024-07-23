package net.mafuyu33.mafishmod.mixin.itemmixin.colliableitem;


import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.Settings.class)
public abstract class AbstractBlockMixin {
    @Mutable
    @Shadow boolean collidable;
    @Mutable
    @Shadow boolean opaque;
    @Mutable
    @Shadow boolean dynamicBounds;
    @Inject(method = "noCollision", at = @At("HEAD"),cancellable = true)
    private void init(CallbackInfoReturnable<AbstractBlock.Settings> cir) {
//        if(!dynamicBounds) {
            dynamicBounds = true;
            collidable = true;
            opaque = false;
            cir.setReturnValue((AbstractBlock.Settings)(Object)this);
//        }
    }
}
