package player.stevemod;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().getString();

        if (message.equalsIgnoreCase("help")) {
            if (!player.level().isClientSide()) {
                SteveEntity steve = new SteveEntity(ModEntities.STEVE.get(), player.level());
                steve.setPos(player.getX(), player.getY() + 1, player.getZ());
                steve.setOwner(player);
                player.level().addFreshEntity(steve);
                System.out.println("Steve spawned at: " + player.getX() + ", " + player.getY() + ", " + player.getZ());
            }
        } else if (message.equalsIgnoreCase("back")) {
            player.level().getEntitiesOfClass(SteveEntity.class, player.getBoundingBox().inflate(100))
                    .forEach(Entity::discard);
        }
    }
}
