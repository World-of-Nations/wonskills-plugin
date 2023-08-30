package fr.holo795.wonskill.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.holo795.wonskill.WonSkill;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class PacketListener implements PluginMessageListener {

    private final WonSkill plugin;

    public PacketListener(WonSkill plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] byteMessage) {

        ByteArrayDataInput in = ByteStreams.newDataInput(byteMessage);
        byte subChannel = in.readByte();

        if (subChannel == PacketType._BUILDER.getPacketId()) {
            String message = in.readLine();

            PacketType packet = PacketType.from(Integer.parseInt(message));
            if (packet == PacketType.NO_PACKET) throwIllegalPacket(player, message);

            switch (packet) {
                case USERINFO -> plugin.getPacketHelper().sendPacketUserinfo(player);
                default -> throw new IllegalStateException("Error while handling packet: " + packet.name());
            }


        }

    }

    private void throwIllegalPacket(Player player, String message) throws IllegalStateException {
        throw new IllegalStateException("Illegal packet from %s : %s".formatted(player.getName(), message));
    }
}
