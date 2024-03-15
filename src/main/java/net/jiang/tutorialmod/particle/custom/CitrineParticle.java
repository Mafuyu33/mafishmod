package net.jiang.tutorialmod.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class CitrineParticle extends SpriteBillboardParticle {
    protected CitrineParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                              SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0F;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.scale *= 0.1F;
        this.maxAge = 2000000000;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.velocityY -= 0.04 * (double)this.gravityStrength;
            this.move(0.0000001, 0.0000001, 0.0000001);
            if (this.ascending && this.y == this.prevPosY) {
                this.velocityX *= 1.1;
                this.velocityZ *= 1.1;
            }

            this.velocityX *= (double)this.velocityMultiplier;
            this.velocityY *= (double)this.velocityMultiplier;
            this.velocityZ *= (double)this.velocityMultiplier;
            if (this.onGround) {
                this.velocityX *= 0.699999988079071;
                this.velocityZ *= 0.699999988079071;
            }

        }
//        fadeOut();
    }

//    private void fadeOut() {
//        this.alpha = (-(1/(float)maxAge) * age + 1);
//    }

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
            return new CitrineParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}