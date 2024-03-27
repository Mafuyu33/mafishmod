package net.mafuyu33.mafishmod.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {


    @Shadow @Final @Deprecated private Block block;

    @Shadow public abstract Block getBlock();

    public BlockItemMixin(Settings settings) {
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
            return true;
    }

}

