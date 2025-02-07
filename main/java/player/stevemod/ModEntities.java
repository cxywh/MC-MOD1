package player.stevemod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<SteveEntity>> STEVE = ENTITIES.register(
            "steve",
            () -> EntityType.Builder.of(SteveEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F)
                    .build("steve")
    );
}