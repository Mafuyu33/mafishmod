package net.jiang.tutorialmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class LightningRodMixin {

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void onPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = context.getStack();
        // 检查是否有特定的附魔，例如我们检查"锋利"附魔作为示例
        int k = EnchantmentHelper.getLevel(Enchantments.CHANNELING, itemStack);
        if (k > 0) {
            // 在这里改变放置逻辑，例如取消放置

            cir.setReturnValue(ActionResult.FAIL);

            // 或者根据附魔执行特定逻辑，比如放置一个带有特殊行为的方块实体
        }
    }
}
