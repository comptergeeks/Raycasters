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

    public boolean checkCollision(GameMap m) {
        int xOffset = 0;
        if (playerDeltaX < 0) {
            xOffset = -20;
        } else {
            xOffset = 20;
        }
        int yOffset = 0;
        if (playerDeltaY < 0) {
            yOffset = -20;
        } else {
            yOffset = 20;
        }

        int playerGridPositionX = (int) (playerX / 64.0);
        int playerGridPositionXOffset = (int) ((playerX + xOffset) / 64);
        int playerGridPositionNegXOffset = (int) ((playerX - xOffset) / 64);

        int playerGridPositionY = (int) (playerY / 64.0);
        int playerGridPositionYOffset = (int) ((playerY + yOffset) / 64);
        int playerGridPositionNegYOffset = (int) ((playerY - yOffset) / 64);
    }

    public boolean checkPosY() {
        if (m.mapDisplay[playerGridPositionYOffset * m.mapX + playerGridPositionX] == 0) {
            return true;
        }
    }

    public boolean checkPosX() {
        if (m.mapDisplay[playerGridPositionY * m.mapX + playerGridPositionXOffset] == 0) {
            return true;
        }
    }
}

