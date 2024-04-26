package net.mafuyu33.mafishmod.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class ALeaf implements HudRenderCallback{
    float totalTickDelta;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        World world= MinecraftClient.getInstance().world;
        int width = drawContext.getScaledWindowWidth();
        int height = drawContext.getScaledWindowHeight();
        totalTickDelta += tickDelta;
        if(world!=null) {
            List<? extends PlayerEntity> players = world.getPlayers();
            for (PlayerEntity player : players) {
                ItemStack currentHelmet = player.getInventory().getArmorStack(3);
                if (currentHelmet.getItem() instanceof ArmorItem && ((ArmorItem) currentHelmet.getItem()).getType() == ArmorItem.Type.HELMET) {
                    int i = EnchantmentHelper.getLevel(ModEnchantments.A_LEAF, currentHelmet);
                    if (i > 0) {
                        Identifier texture = new Identifier("mafishmod", "textures/gui/a_leaf.png");
                        float k = 0.03f;
                        int w = (int) (((int) totalTickDelta*k));
                        int h = (int) (((int) totalTickDelta*k));
                        // texture, x, y, u, v, width, height, textureWidth, textureHeight
                        drawContext.drawTexture(texture, width/2-w/2, height/2-h/2, 0, 0, w, h, w, h);
                    }
                }
                if(currentHelmet.isEmpty()){
                    totalTickDelta=0;
                }
            }
        }




//        MatrixStack matrices = drawContext.getMatrices();
//
//        // Store the total tick delta in a field, so we can use it later.
//        totalTickDelta += tickDelta;
//
//        // Push a new matrix onto the stack.
//        matrices.push();
//        // Lerp between 0 and 360 degrees over time.
//        float rotationAmount = (float) (totalTickDelta / 50F % 360);
//        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(rotationAmount));
//        // Shift entire diamond so that it rotates in its center.
//        matrices.translate(20f, 40f, 0f);
//
//        // Get the transformation matrix from the matrix stack, alongside the tessellator instance and a new buffer builder.
//        Matrix4f transformationMatrix = drawContext.getMatrices().peek().getPositionMatrix();
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder buffer = tessellator.getBuffer();
//
//        // Initialize the buffer using the specified format and draw mode.
//        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
//
//        // Write our vertices, Z doesn't really matter since it's on the HUD.
//        buffer.vertex(transformationMatrix, 20, 20, 5).color(0xFF414141).next();
//        buffer.vertex(transformationMatrix, 5, 40, 5).color(0xFF000000).next();
//        buffer.vertex(transformationMatrix, 35, 40, 5).color(0xFF000000).next();
//        buffer.vertex(transformationMatrix, 20, 60, 5).color(0xFF414141).next();
//
//        // We'll get to this bit in the next section.
//        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//
//        // Draw the buffer onto the screen.
//        tessellator.draw();
//
//        // Pop our matrix from the stack.
//        matrices.pop();
    }
}
