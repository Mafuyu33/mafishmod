package net.mafuyu33.mafishmod.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class RubberParticle extends SpriteBillboardParticle {
    protected RubberParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                             SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0F;
        this.red = (float) xd;
        this.green = (float) yd;
        this.blue = (float) zd;
        this.scale *= 1F;
        this.maxAge = 1;
        this.setSpriteForAge(spriteSet);

        this.setColor(red, green, blue); // 设置颜色
    }

    @Override
    public float getSize(float tickDelta) {
        return 0.013F;
    }

    @Override
    public void tick() {
        if (this.age++ >= this.maxAge) {
            this.markDead();
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new RubberParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}