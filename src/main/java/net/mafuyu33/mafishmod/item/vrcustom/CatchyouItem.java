//package net.mafuyu33.mafishmod.item.vrcustom;
//
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.text.Text;
//import net.minecraft.util.Hand;
//import net.minecraft.util.TypedActionResult;
//import net.minecraft.world.World;
//import org.vivecraft.api_beta.VivecraftAPI;
//import org.vivecraft.client_vr.provider.MCVR;
//
//public class CatchyouItem extends Item {
//    public CatchyouItem(Settings settings) {
//        super(settings);
//    }
//
//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        return super.use(world, user, hand);
//        if(MCVR.get().isActive()){
//            user.sendMessage(Text.literal((String.valueOf(123))),false);
//        }
//    }
//}
