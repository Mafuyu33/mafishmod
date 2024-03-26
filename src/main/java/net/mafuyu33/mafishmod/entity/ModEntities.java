package net.mafuyu33.mafishmod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.mafuyu33.mafishmod.TutorialMod;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<TNTProjectileEntity> TNT_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(TutorialMod.MOD_ID, "tnt_projectile"),
            FabricEntityTypeBuilder.<TNTProjectileEntity>create(SpawnGroup.MISC, TNTProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static final EntityType<TNTProjectileEntity> STONE_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(TutorialMod.MOD_ID, "stone_projectile"),
            FabricEntityTypeBuilder.<TNTProjectileEntity>create(SpawnGroup.MISC, TNTProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static final EntityType<TNTProjectileEntity> FU_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(TutorialMod.MOD_ID, "fu_projectile"),
            FabricEntityTypeBuilder.<TNTProjectileEntity>create(SpawnGroup.MISC, TNTProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static final EntityType<TNTProjectileEntity> FIREWORK_ARROW = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(TutorialMod.MOD_ID, "firework_arrow"),
            FabricEntityTypeBuilder.<TNTProjectileEntity>create(SpawnGroup.MISC, TNTProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static final EntityType<TNTProjectileEntity> DIAMOND_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(TutorialMod.MOD_ID, "diamond_projectile"),
            FabricEntityTypeBuilder.<TNTProjectileEntity>create(SpawnGroup.MISC, TNTProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());
    public static final EntityType<LightningProjectileEntity> LIGHTNING_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(TutorialMod.MOD_ID, "lightning_projectile"),
            FabricEntityTypeBuilder.<LightningProjectileEntity>create(SpawnGroup.MISC, LightningProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

}