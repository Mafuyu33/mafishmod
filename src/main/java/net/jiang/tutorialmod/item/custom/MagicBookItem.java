package net.jiang.tutorialmod.item.custom;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.world.World;

public class MagicBookItem extends WrittenBookItem {

    public MagicBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(!stack.hasEnchantments() && world.isClient){
            stack.addEnchantment(Enchantments.MENDING,1);
            addPageContent(stack, "\"123444\"");
            addPageContent(stack, "\"第二页\"");
        }
    }

    public static void addPageContent(ItemStack stack, String content) {
        // 获取书的NBT数据
        NbtCompound nbt = stack.getOrCreateNbt();

        // 获取书的页面列表
        NbtList pagesList = nbt.getList("pages", NbtType.STRING);

        // 将内容添加到页面列表中
        pagesList.add(NbtString.of(content));

        // 更新书的NBT数据中的页面列表
        nbt.put("pages", pagesList);
        // 添加作者信息
        nbt.putString("author","Mafish");
        nbt.putString("filtered_title","魔法书叔叔");

        // 添加标题信息
        nbt.putString("title", "魔法书叔叔");

        // 标记书籍为已解析
        nbt.putBoolean("resolved", true);
    }

    // 重写 getName 方法以显示魔法书籍的标题，并添加特殊颜色
//    @Override
//    public Text getName(ItemStack stack) {
//        Text baseName = super.getName(stack);
//        NbtCompound nbtCompound = stack.getNbt();
//        if (nbtCompound != null) {
//            String title = nbtCompound.getString("魔典");
//            if (!title.isEmpty()) {
//                baseName = baseName.copy().formatted(Formatting.LIGHT_PURPLE); // 设置标题颜色为浅紫色
//                return baseName.copy().append(Text.of(": " + title)); // 添加标题内容
//            }
//        }
//        return baseName;
//    }

}
