package net.mafuyu33.mafishmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;


public class ModRecipeProvider extends FabricRecipeProvider {
//    private static final List<ItemConvertible> RUBY_SMELTABLES = List.of(ModItems.RAW_RUBY,
//            ModBlocks.RUBY_BLOCK,ModBlocks.RAW_RUBY_BLOCK);
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
//        offerSmelting(exporter,RUBY_SMELTABLES, RecipeCategory.MISC,ModItems.RUBY,
//                0.7f,200,"ruby");
//        offerBlasting(exporter,RUBY_SMELTABLES, RecipeCategory.MISC,ModItems.RUBY,
//                0.7f,100,"ruby");
//
//        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.RUBY, RecipeCategory.DECORATIONS,
//                ModBlocks.RUBY_BLOCK);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC,ModItems.RAW_RUBY,1)
//                .pattern("SSS")
//                .pattern("SRS")
//                .pattern("SSS")
//                .input('S', Items.STONE)
//                .input('R', ModItems.RUBY)
//                .criterion(hasItem(ModBlocks.WHATE_CAT_BLOCK),conditionsFromItem(ModBlocks.WHATE_CAT_BLOCK))
//                .criterion(hasItem(ModItems.RUBY),conditionsFromItem(ModItems.RUBY))
//                .offerTo(exporter, new Identifier(getRecipeName(ModItems.RAW_RUBY)));


    }
}
