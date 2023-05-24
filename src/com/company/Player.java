package com.company;

public class Player {
    float playerX;
    float playerY;
    float playerDeltaX;
    float playerDeltaY;
    float playerAngle;

    public Player(float playerX, float playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }
    public void setDeltas(float playerDeltaX, float playerDeltaY) {
        this.playerDeltaX = playerDeltaX;
        this.playerDeltaY = playerDeltaY;

    }
}
