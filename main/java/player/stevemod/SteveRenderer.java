package player.stevemod;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SteveRenderer extends MobRenderer<SteveEntity, SteveModel<SteveEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/steve.png");

    public SteveRenderer(EntityRendererProvider.Context context) {
        super(context, new SteveModel<>(context.bakeLayer(SteveModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SteveEntity entity) {
        return TEXTURE;
    }
}