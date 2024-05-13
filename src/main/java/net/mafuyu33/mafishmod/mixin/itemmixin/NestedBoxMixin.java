package net.mafuyu33.mafishmod.mixin.itemmixin;

import net.mafuyu33.mafishmod.util.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(BlockItem.class)
public abstract class NestedBoxMixin extends Item {


    @Shadow @Final @Deprecated private Block block;

    @Shadow public abstract Block getBlock();

    public NestedBoxMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author
     * Mafuyu33
     * @reason
     * 让潜影盒能放进潜影盒里
     */
    @Overwrite
    public boolean canBeNested() {
        boolean isNestedBoxInfinite = ConfigHelper.isNestedBoxInfinite();
        if(isNestedBoxInfinite){
            return true;
        }else{
            return !(this.block instanceof ShulkerBoxBlock);
        }
    }

}

