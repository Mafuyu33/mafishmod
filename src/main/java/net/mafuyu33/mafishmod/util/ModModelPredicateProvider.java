//package net.mafuyu33.mafishmod.util;
//
//import net.mafuyu33.mafishmod.item.ModItems;
//import net.minecraft.client.item.ModelPredicateProviderRegistry;
//import net.minecraft.item.CrossbowItem;
//import net.minecraft.item.Item;
//import net.minecraft.item.Items;
//import net.minecraft.util.Identifier;
//
//public class ModModelPredicateProvider {
//    public static void registerModModels() {
//        registerCrossBow(ModItems.ZHUGE);
//    }
//
//    private static void registerBow(Item bow) {
//        ModelPredicateProviderRegistry.register(bow, new Identifier("pull"),
//                (stack, world, entity, seed) -> {
//                    if (entity == null) {
//                        return 0.0f;
//                    }
//                    if (entity.getActiveItem() != stack) {
//                        return 0.0f;
//                    }
//                    return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0f;
//                });
//
//        ModelPredicateProviderRegistry.register(bow, new Identifier("pulling"),
//                (stack, world, entity, seed) -> entity != null && entity.isUsingItem()
//                        && entity.getActiveItem() == stack ? 1.0f : 0.0f);
//    }
//    private static void registerCrossBow(Item crossbow){
//        ModelPredicateProviderRegistry.register(crossbow, new Identifier("pull"), (stack, world, entity, seed) -> {
//            if (entity == null) {
//                return 0.0F;
//            } else {
//                return CrossbowItem.isCharged(stack) ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / (float)CrossbowItem.getPullTime(stack);
//            }
//        });
//        ModelPredicateProviderRegistry.register(crossbow, new Identifier("pulling"), (stack, world, entity, seed) -> {
//        return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
//        });
//        ModelPredicateProviderRegistry.register(crossbow, new Identifier("charged"), (stack, world, entity, seed) -> {
//        return CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
//        });
//        ModelPredicateProviderRegistry.register(crossbow, new Identifier("firework"), (stack, world, entity, seed) -> {
//        return CrossbowItem.isCharged(stack) && CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
//        });
//    }
//}
