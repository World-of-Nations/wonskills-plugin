package fr.holo795.wonskill.packets;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.holo795.wonskill.WonSkill;
import fr.holo795.wonskill.users.User;
import org.bukkit.entity.Player;

public class PacketHelper {

    public final String CHANNEL_NAME = "wonskill:main_channel";
    private final WonSkill plugin;

    public PacketHelper(WonSkill plugin) {
        this.plugin = plugin;
    }

    public void sendPacketUserinfo(Player target) {

        ByteArrayDataOutput out = newDataOutput(PacketType.USERINFO);

        User user = plugin.getUserData().getUser(target);
        if (user == null) {
            out.writeUTF("null");
            sendTo(target, out.toByteArray());
            return;
        }

        out.writeUTF(user.getUuid());
        out.writeUTF(String.valueOf(user.getJobID()));
        out.writeUTF(String.valueOf(user.getXp()));
        out.writeUTF(String.valueOf(user.getPoint()));

        StringBuilder skills = new StringBuilder();
        for (int i = 0; i < user.getUnlockedSkills().size(); i++) {
            skills.append(user.getUnlockedSkills().get(i));
            if (i != user.getUnlockedSkills().size() - 1) skills.append(",");
        }
        out.writeUTF(skills.toString());

        StringBuilder actions = new StringBuilder();
        for (int i = 0; i < user.getActionData().size(); i++) {
            actions.append(user.getActionData().get(i).getId()).append(":").append(user.getActionData().get(i).getAmount());
            if (i != user.getActionData().size() - 1) actions.append(",");
        }
        out.writeUTF(actions.toString());

        sendTo(target, out.toByteArray());

    }

    private ByteArrayDataOutput newDataOutput(PacketType packetType) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeByte(packetType.getPacketId());
        return out;
    }

    private void sendTo(Player target, byte[] message) {
        target.sendPluginMessage(this.plugin, CHANNEL_NAME, message);
    }


}
