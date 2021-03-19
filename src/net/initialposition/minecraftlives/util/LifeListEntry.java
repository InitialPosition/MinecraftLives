package net.initialposition.minecraftlives.util;

import java.util.UUID;

public class LifeListEntry {

    private final UUID id;
    private int lives;

    public LifeListEntry(UUID id, int lives) {
        this.id = id;
        this.lives = lives;
    }

    public UUID getUUID() {
        return this.id;
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int newLifes) {
        this.lives = newLifes;
    }
}
