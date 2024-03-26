//package net.mafuyu33.mafishmod.item.custom;
//
//import net.minecraft.entity.attribute.EntityAttributeInstance;
//import net.minecraft.entity.effect.StatusEffect;
//import net.minecraft.entity.effect.StatusEffectCategory;
//import net.minecraft.entity.effect.StatusEffects;
//import net.minecraft.registry.Registries;
//import net.minecraft.registry.Registry;
//import net.minecraft.util.Identifier;
//
//import static net.mafuyu33.mafishmod.TutorialMod.MOD_ID;
//
//
//public class ModStatusEffects{
//    public static final StatusEffect IRONMAN = new IronStatusEffect();
//
//    // 构造函数为空的私有类，用于表示IRON效果
//        private static class IronStatusEffect extends StatusEffect {
//            public IronStatusEffect() {
//                super(StatusEffectCategory.BENEFICIAL, 0xFF0000); // 设置类别和颜色（红色）
//            }
//        }
//
//
//    private static void ironAttributeModifiers(EntityAttributeInstance instance) {
//        // 可以在这里添加自定义效果对实体属性的修改
//    }
//
//    public static void registerModEffect(){
//    Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "ironman"), ModStatusEffects.IRONMAN);
//    }
//
//}
