package player.stevemod;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class RevivePacket {
    private final int entityId;

    public RevivePacket(int entityId) {
        this.entityId = entityId;
    }

    public static void encode(RevivePacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
    }

    public static RevivePacket decode(FriendlyByteBuf buf) {
        return new RevivePacket(buf.readInt());
    }

    public static void handle(RevivePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Entity entity = player.level().getEntity(packet.entityId);
                if (entity instanceof SteveEntity steve) {
                    steve.revive();
                    player.sendSystemMessage(Component.literal("Steve has been revived!"));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sendRevivePacket(int entityId) {
        ExampleMod.CHANNEL.send(PacketDistributor.SERVER.noArg(), new RevivePacket(entityId));
    }
}
