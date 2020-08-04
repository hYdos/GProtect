package io.github.hydos.gprotect.config;

import java.util.Date;

public class BlockHistory {

    String playerUuid;
    int id;
    String oldBlock;
    String newBlock;
    long position;
    Date time;

    public BlockHistory(int id, String oldBlock, String newBlock, long position, Date time, String playerUuid) {
        this.id = id;
        this.oldBlock = oldBlock;
        this.newBlock = newBlock;
        this.position = position;
        this.time = time;
        this.playerUuid = playerUuid;
    }

    public int getId() {
        return id;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public String getOldBlock() {
        return oldBlock;
    }

    public String getNewBlock() {
        return newBlock;
    }

    public long getPosition() {
        return position;
    }

    public Date getTime() {
        return time;
    }
}
