package com.st0x0ef.wonskill.packets;

public enum PacketType {

    NO_PACKET(-1),

    _BUILDER(22),

    USERINFO(1),
    SKILl_INFO(2),
    ACTION_INFO(3),
    JOB_INFO(4),
    ALL(5),

    OPEN_GUI(0);


    private final int id;

    PacketType(int id) {
        this.id = id;
    }

    public static PacketType from(int packetId) {
        for (PacketType packetType : values()) {
            if (packetType.getPacketId() == packetId) {
                return packetType;
            }
        }

        return NO_PACKET;
    }

    public int getPacketId() {
        return id;
    }
}
